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

    // =========================================================
    // 1. [신규 추가] 전자 결재용 완벽 호환 메서드 모음
    // =========================================================

    // 1-1. 비품 대여 신청 (날짜 컬럼, 기안 제목 포함 및 대여중 즉시 재고 차감)
    public boolean insertRental(RentalHistoryDTO dto) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtEq = null;

        String sql = "INSERT INTO RENTAL_HISTORY (RENTAL_NO, TITLE, EMP_NO, EQ_NO, RENTAL_DATE, RETURN_DATE, STATUS, APPROVAL_STEP, "
                   + "SIGN1, SIGN1_DATE, SIGN2, SIGN2_DATE, SIGN3, SIGN3_DATE, SIGN4, SIGN4_DATE, SIGN5, SIGN5_DATE) "
                   + "VALUES (SEQ_RENTAL.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); 

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getTitle());
            pstmt.setInt(2, dto.getEmpNo());
            pstmt.setInt(3, dto.getEqNo());
            pstmt.setDate(4, dto.getRentalDate());
            pstmt.setDate(5, dto.getReturnDate());
            pstmt.setString(6, dto.getStatus());
            pstmt.setInt(7, dto.getApprovalStep());
            
            pstmt.setString(8, dto.getSign1());
            pstmt.setDate(9, dto.getSign1Date());
            pstmt.setString(10, dto.getSign2());
            pstmt.setDate(11, dto.getSign2Date());
            pstmt.setString(12, dto.getSign3());
            pstmt.setDate(13, dto.getSign3Date());
            pstmt.setString(14, dto.getSign4());
            pstmt.setDate(15, dto.getSign4Date());
            pstmt.setString(16, dto.getSign5());
            pstmt.setDate(17, dto.getSign5Date());

            int count = pstmt.executeUpdate();

            if (count > 0) {
                if ("대여중".equals(dto.getStatus())) {
                    String updateEqSql = "UPDATE EQUIPMENT SET REMAIN_COUNT = REMAIN_COUNT - 1 WHERE EQ_NO = ? AND REMAIN_COUNT > 0";
                    pstmtEq = conn.prepareStatement(updateEqSql);
                    pstmtEq.setInt(1, dto.getEqNo());
                    
                    int eqCount = pstmtEq.executeUpdate();
                    if (eqCount > 0) {
                        conn.commit();
                        result = true;
                    } else {
                        conn.rollback(); 
                    }
                } else {
                    conn.commit(); 
                    result = true;
                }
            }
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, null);
            if (pstmtEq != null) try { pstmtEq.close(); } catch(Exception e) {}
        }
        return result;
    }

    // 1-2. 모든 기안 문서(전자 결재) 목록 조회 (모든 등급 열람용)
    public List<RentalHistoryDTO> getAllDocumentList() {
        List<RentalHistoryDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT h.*, e.EMP_NAME, e.EMP_LEVEL, eq.EQ_NAME, eq.TOTAL_COUNT, eq.REMAIN_COUNT "
                   + "FROM RENTAL_HISTORY h "
                   + "LEFT JOIN EMPLOYEE e ON h.EMP_NO = e.EMP_NO "
                   + "LEFT JOIN EQUIPMENT eq ON h.EQ_NO = eq.EQ_NO "
                   + "ORDER BY h.RENTAL_NO DESC";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
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

                    dto.setTitle(rs.getString("TITLE"));
                    dto.setSign1Date(rs.getDate("SIGN1_DATE"));
                    dto.setSign2Date(rs.getDate("SIGN2_DATE"));
                    dto.setSign3Date(rs.getDate("SIGN3_DATE"));
                    dto.setSign4Date(rs.getDate("SIGN4_DATE"));
                    dto.setSign5Date(rs.getDate("SIGN5_DATE"));

                    dto.setEmpName(rs.getString("EMP_NAME"));
                    dto.setEmpLevel(rs.getInt("EMP_LEVEL"));

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

    // 1-3. 특정 기안 문서의 상세 정보 및 비품 현황 조회 (결재 상세 페이지용)
    public RentalHistoryDTO getDocumentDetail(int rentalNo) {
        RentalHistoryDTO dto = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT h.*, e.EMP_NAME, e.EMP_LEVEL, eq.EQ_NAME, eq.TOTAL_COUNT, eq.REMAIN_COUNT "
                + "FROM RENTAL_HISTORY h " + "LEFT JOIN EMPLOYEE e ON h.EMP_NO = e.EMP_NO "
                + "LEFT JOIN EQUIPMENT eq ON h.EQ_NO = eq.EQ_NO " + "WHERE h.RENTAL_NO = ?";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, rentalNo);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    dto = new RentalHistoryDTO();
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
                    dto.setTitle(rs.getString("TITLE"));
                    dto.setSign1Date(rs.getDate("SIGN1_DATE"));
                    dto.setSign2Date(rs.getDate("SIGN2_DATE"));
                    dto.setSign3Date(rs.getDate("SIGN3_DATE"));
                    dto.setSign4Date(rs.getDate("SIGN4_DATE"));
                    dto.setSign5Date(rs.getDate("SIGN5_DATE"));

                    dto.setEmpName(rs.getString("EMP_NAME"));
                    dto.setEmpLevel(rs.getInt("EMP_LEVEL"));

                    dto.setEqName(rs.getString("EQ_NAME"));
                    dto.setTotalCount(rs.getInt("TOTAL_COUNT"));
                    dto.setRemainCount(rs.getInt("REMAIN_COUNT"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, rs);
        }
        return dto;
    }

    // 1-4. 통합 3분할 결재 승인/반려 프로세스 처리
    public boolean processApproval(int rentalNo, int eqNo, int step, String empName, boolean isApprove) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtEq = null;

        String signCol = "SIGN" + step;
        String dateCol = "SIGN" + step + "_DATE";
        
        String status = isApprove ? "승인대기" : "반려됨";
        int nextStep = isApprove ? step + 1 : step;

        if (isApprove && step == 5) {
            status = "대여중";
        }

        String sql = "UPDATE RENTAL_HISTORY SET " + signCol + " = ?, " + dateCol + " = SYSDATE, STATUS = ?, APPROVAL_STEP = ? WHERE RENTAL_NO = ?";
        String updateEqSql = "UPDATE EQUIPMENT SET REMAIN_COUNT = REMAIN_COUNT - 1 WHERE EQ_NO = ? AND REMAIN_COUNT > 0";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); 

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, empName);
            pstmt.setString(2, status);
            pstmt.setInt(3, nextStep);
            pstmt.setInt(4, rentalNo);
            
            int count = pstmt.executeUpdate();

            if (count > 0 && isApprove && step == 5) {
                pstmtEq = conn.prepareStatement(updateEqSql);
                pstmtEq.setInt(1, eqNo);
                
                int eqCount = pstmtEq.executeUpdate();
                if (eqCount > 0) {
                    conn.commit();
                    result = true;
                } else {
                    conn.rollback(); 
                }
            } else if (count > 0) {
                conn.commit(); 
                result = true;
            }

        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, null);
            if (pstmtEq != null) try { pstmtEq.close(); } catch(Exception e) {}
        }
        return result;
    }


    // =========================================================
    // 2. [복구 완료] 기존 컨트롤러에서 사용 중이던 레거시 메서드 모음
    // =========================================================

    // 2-1. 관리자 결재함 조회 (ManagerApprovalController 사용)
    public List<RentalHistoryDTO> getPendingList(int managerLevel) {
        List<RentalHistoryDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

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
                    dto.setTitle(rs.getString("TITLE")); // 기안 제목 추가 매핑
                    dto.setRentalDate(rs.getDate("RENTAL_DATE"));
                    dto.setReturnDate(rs.getDate("RETURN_DATE"));
                    dto.setStatus(rs.getString("STATUS"));
                    dto.setApprovalStep(rs.getInt("APPROVAL_STEP"));
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

    // 2-2. 개인 대여 내역 조회 (MyRentalListController 사용)
    public List<RentalHistoryDTO> getMyRentalList(int empNo) {
        List<RentalHistoryDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT h.*, e.EQ_NAME FROM RENTAL_HISTORY h JOIN EQUIPMENT e ON h.EQ_NO = e.EQ_NO WHERE h.EMP_NO = ? ORDER BY h.RENTAL_NO DESC";

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
                    dto.setEqName(rs.getString("EQ_NAME")); 
                    dto.setTitle(rs.getString("TITLE")); // 기안 제목 추가 매핑
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

    // 2-3. 결재 상태 강제 업데이트 (ReturnProcessController 반납 처리 사용)
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

    // 2-4. 다단계 결재 처리 (ApprovalProcessController 사용)
    public boolean processStepApproval(int rentalNo, int currentStep, String managerName, String action) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "";

            if ("approve".equals(action)) {
                if (currentStep < 5) {
                    // 승인 날짜 기록 포함
                    sql = "UPDATE RENTAL_HISTORY SET SIGN" + currentStep + " = ?, SIGN" + currentStep + "_DATE = SYSDATE, APPROVAL_STEP = ? WHERE RENTAL_NO = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, managerName);
                    pstmt.setInt(2, currentStep + 1);
                    pstmt.setInt(3, rentalNo);
                } else {
                    sql = "UPDATE RENTAL_HISTORY SET SIGN5 = ?, SIGN5_DATE = SYSDATE, STATUS = '대여중' WHERE RENTAL_NO = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, managerName);
                    pstmt.setInt(2, rentalNo);
                }
            } else {
                // 반려 시 서명 및 날짜 초기화
                sql = "UPDATE RENTAL_HISTORY SET SIGN1=NULL, SIGN1_DATE=NULL, SIGN2=NULL, SIGN2_DATE=NULL, SIGN3=NULL, SIGN3_DATE=NULL, SIGN4=NULL, SIGN4_DATE=NULL, SIGN5=NULL, SIGN5_DATE=NULL, "
                        + "APPROVAL_STEP = 1, STATUS = '반려됨' WHERE RENTAL_NO = ?";
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

    // 공통 자원 해제 메서드
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