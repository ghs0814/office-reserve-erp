package com.office.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.office.dto.RentalHistoryDTO;
import com.office.util.DBConnection;

public class RentalDAO {

	// 1. 비품 대여 신청 (1단계 자동 서명 및 2단계 상신)
    public boolean insertRental(RentalHistoryDTO dto) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        String sql = "INSERT INTO RENTAL_HISTORY (RENTAL_NO, EMP_NO, EQ_NO, RENTAL_DATE, RETURN_DATE, STATUS, APPROVAL_STEP, SIGN1) "
                   + "VALUES (SEQ_RENTAL.NEXTVAL, ?, ?, ?, ?, '승인대기', ?, ?)";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, dto.getEmpNo());
                pstmt.setInt(2, dto.getEqNo());
                pstmt.setDate(3, dto.getRentalDate());
                pstmt.setDate(4, dto.getReturnDate());
                pstmt.setInt(5, dto.getApprovalStep());
                pstmt.setString(6, dto.getSign1());

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

    // 2. 관리자 결재함 (본인 등급에 맞는 승인 대기 목록만 조회)
    public List<RentalHistoryDTO> getPendingList(int managerLevel) {
        List<RentalHistoryDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        // APPROVAL_STEP이 현재 관리자 레벨과 일치하는 것만 가져옴
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

    // 3. 결재 상태 업데이트 (승인, 반려, 반납 등)
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
 // RentalDAO.java 내부에 추가할 결재 처리 공통 메서드
    public boolean processStepApproval(int rentalNo, int currentStep, String managerName, String action) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "";

            if ("approve".equals(action)) {
                if (currentStep < 5) {
                    sql = "UPDATE RENTAL_HISTORY SET SIGN" + currentStep + " = ?, APPROVAL_STEP = ? WHERE RENTAL_NO = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, managerName);
                    pstmt.setInt(2, currentStep + 1);
                    pstmt.setInt(3, rentalNo);
                } else {
                    sql = "UPDATE RENTAL_HISTORY SET SIGN5 = ?, STATUS = '대여중' WHERE RENTAL_NO = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, managerName);
                    pstmt.setInt(2, rentalNo);
                }
            } else {
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
    // 내 대여 목록 가져오기
 // 내 대여 내역 목록 가져오기 (비품 이름 포함)
    public List<RentalHistoryDTO> getMyRentalList(int empNo) {
        List<RentalHistoryDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 대여 기록과 비품 테이블을 조인하여 비품명(EQ_NAME)을 함께 가져옵니다.
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
                    dto.setEqName(rs.getString("EQ_NAME")); // 조인해서 가져온 이름 세팅
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
    // 자원 해제 공통 메서드
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