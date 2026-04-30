package com.office.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.office.dto.RentalHistoryDTO;
import com.office.util.DBConnection;

public class RentalDAO {

    // 1. 비품 대여 신청 (기안 올리기)
    public boolean insertRental(RentalHistoryDTO dto) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        String sql = "INSERT INTO RENTAL_HISTORY (RENTAL_NO, EMP_NO, EQ_NO, RENTAL_DATE, RETURN_DATE, STATUS, APPROVAL_STEP) "
                   + "VALUES (SEQ_RENTAL.NEXTVAL, ?, ?, ?, ?, '승인대기', 1)";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, dto.getEmpNo());
                pstmt.setInt(2, dto.getEqNo());
                pstmt.setDate(3, dto.getRentalDate());
                pstmt.setDate(4, dto.getReturnDate());

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

    // 2. 관리자 결재함 (승인 대기 중인 목록 조회)
    public List<RentalHistoryDTO> getPendingList() {
        List<RentalHistoryDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT * FROM RENTAL_HISTORY WHERE STATUS = '승인대기' ORDER BY RENTAL_DATE ASC";

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