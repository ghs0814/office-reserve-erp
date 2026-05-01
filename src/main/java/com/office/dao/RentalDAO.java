package com.office.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.office.dto.RentalHistoryDTO;
import com.office.util.DBConnection;

/**
 * 비품 대여 및 다단계 결재 프로세스를 관리하는 DAO입니다.
 */
public class RentalDAO {

	// 1. 비품 대여 신청 (등급별 동적 상태 및 다중 서명 처리)
	public boolean insertRental(RentalHistoryDTO dto) {
	    boolean result = false;
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    
	    // STATUS를 ?로 변경하고 SIGN1~SIGN5 컬럼을 모두 추가합니다.
	    String sql = "INSERT INTO RENTAL_HISTORY (RENTAL_NO, EMP_NO, EQ_NO, RENTAL_DATE, RETURN_DATE, STATUS, APPROVAL_STEP, SIGN1, SIGN2, SIGN3, SIGN4, SIGN5) "
	               + "VALUES (SEQ_RENTAL.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	    try {
	        conn = DBConnection.getConnection();
	        if (conn != null) {
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setInt(1, dto.getEmpNo());
	            pstmt.setInt(2, dto.getEqNo());
	            pstmt.setDate(3, dto.getRentalDate());
	            pstmt.setDate(4, dto.getReturnDate());
	            pstmt.setString(5, dto.getStatus());      // 컨트롤러에서 설정한 상태값 (승인대기 또는 대여중)
	            pstmt.setInt(6, dto.getApprovalStep());   // 설정된 결재 단계
	            pstmt.setString(7, dto.getSign1());       // 서명 1
	            pstmt.setString(8, dto.getSign2());       // 서명 2
	            pstmt.setString(9, dto.getSign3());       // 서명 3
	            pstmt.setString(10, dto.getSign4());      // 서명 4
	            pstmt.setString(11, dto.getSign5());      // 서명 5

	            int count = pstmt.executeUpdate();
	            if (count > 0) result = true;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        closeResource(conn, pstmt, null);
	    }
	    return result;
	}

    // 2. 관리자 결재함 조회 (로그인한 관리자의 등급과 일치하는 승인 대기 목록만 추출)[cite: 37]
    public List<RentalHistoryDTO> getPendingList(int managerLevel) {
        List<RentalHistoryDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        // APPROVAL_STEP이 현재 로그인한 관리자의 등급과 일치하는 문서만 조회합니다.[cite: 37]
        String sql = "SELECT * FROM RENTAL_HISTORY WHERE STATUS = '승인대기' AND APPROVAL_STEP = ? ORDER BY RENTAL_DATE ASC";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, managerLevel);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    RentalHistoryDTO dto = new RentalHistoryDTO();
                    dto.setRentalNo(rs.getInt("RENTAL_NO"));
                    dto.setEmpNo(rs.getInt("EMP_NO"));
                    dto.setEqNo(rs.getInt("EQ_NO"));
                    dto.setRentalDate(rs.getDate("RENTAL_DATE"));
                    dto.setReturnDate(rs.getDate("RETURN_DATE"));
                    dto.setStatus(rs.getString("STATUS"));
                    dto.setApprovalStep(rs.getInt("APPROVAL_STEP"));
                    dto.setSign1(rs.getString("SIGN1"));
                    dto.setSign2(rs.getString("SIGN2"));
                    dto.setSign3(rs.getString("SIGN3"));
                    dto.setSign4(rs.getString("SIGN4"));
                    dto.setSign5(rs.getString("SIGN5"));
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, rs);
        }
        return list;
    }

    // 3. 결재 상태 강제 업데이트 (반납 완료 처리 등 단순 상태 변경 시 사용)[cite: 37]
    public boolean updateStatus(int rentalNo, String status) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        String sql = "UPDATE RENTAL_HISTORY SET STATUS = ? WHERE RENTAL_NO = ?";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, status);
                pstmt.setInt(2, rentalNo);
                
                int count = pstmt.executeUpdate();
                if (count > 0) result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, null);
        }
        return result;
    }

    /**
     * 4. 다단계 결재 처리 공통 메서드
     * 관리자가 승인 시 단계를 높이거나(1~4단계), 최종 승인 처리(5단계)를 수행합니다.[cite: 37]
     */
    public boolean processStepApproval(int rentalNo, int currentStep, String managerName, String action) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "";

            if ("approve".equals(action)) {
                // 승인 시: 5단계 미만이면 서명 기록 후 다음 단계로 상신[cite: 37]
                if (currentStep < 5) {
                    sql = "UPDATE RENTAL_HISTORY SET SIGN" + currentStep + " = ?, APPROVAL_STEP = ? WHERE RENTAL_NO = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, managerName);
                    pstmt.setInt(2, currentStep + 1);
                    pstmt.setInt(3, rentalNo);
                } else {
                    // 최종 5단계 승인 시: 서명 기록 후 상태를 '대여중'으로 변경[cite: 37]
                    sql = "UPDATE RENTAL_HISTORY SET SIGN5 = ?, STATUS = '대여중' WHERE RENTAL_NO = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, managerName);
                    pstmt.setInt(2, rentalNo);
                }
            } else {
                // 반려 시: 모든 서명을 초기화하고 상태를 '반려됨'으로 변경[cite: 37]
                sql = "UPDATE RENTAL_HISTORY SET SIGN1=NULL, SIGN2=NULL, SIGN3=NULL, SIGN4=NULL, SIGN5=NULL, " +
                      "APPROVAL_STEP = 1, STATUS = '반려됨' WHERE RENTAL_NO = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, rentalNo);
            }

            int count = pstmt.executeUpdate();
            if (count > 0) result = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, null);
        }
        return result;
    }

    /**
     * 5. 개인 대여 내역 조회 (비품 마스터 테이블과 조인하여 비품명 포함)[cite: 37]
     */
    public List<RentalHistoryDTO> getMyRentalList(int empNo) {
        List<RentalHistoryDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 조인 쿼리를 통해 대여 기록과 비품의 이름을 한 번에 가져옵니다.[cite: 37]
        String sql = "SELECT h.*, e.EQ_NAME " +
                     "FROM RENTAL_HISTORY h " +
                     "JOIN EQUIPMENT e ON h.EQ_NO = e.EQ_NO " +
                     "WHERE h.EMP_NO = ? " +
                     "ORDER BY h.RENTAL_NO DESC";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, empNo);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    RentalHistoryDTO dto = new RentalHistoryDTO();
                    dto.setRentalNo(rs.getInt("RENTAL_NO"));
                    dto.setEqNo(rs.getInt("EQ_NO"));
                    dto.setEqName(rs.getString("EQ_NAME")); // 조인 결과인 비품명 세팅[cite: 37]
                    dto.setRentalDate(rs.getDate("RENTAL_DATE"));
                    dto.setReturnDate(rs.getDate("RETURN_DATE"));
                    dto.setStatus(rs.getString("STATUS"));
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, rs);
        }
        return list;
    }

    private void closeResource(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}