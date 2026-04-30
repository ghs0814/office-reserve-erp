package com.office.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.office.dto.EmployeeDTO;
import com.office.util.DBConnection;

public class EmployeeDAO {

    // 1. 로그인 체크 (아이디와 비밀번호가 일치하면 사원 정보를 반환)
    public EmployeeDTO loginCheck(String loginId, String loginPw) {
        EmployeeDTO dto = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT * FROM EMPLOYEE WHERE LOGIN_ID = ? AND LOGIN_PW = ?";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, loginId);
                pstmt.setString(2, loginPw);
                
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    dto = new EmployeeDTO();
                    dto.setEmpNo(rs.getInt("EMP_NO"));
                    dto.setLoginId(rs.getString("LOGIN_ID"));
                    dto.setLoginPw(rs.getString("LOGIN_PW"));
                    dto.setEmpName(rs.getString("EMP_NAME"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, rs);
        }
        return dto; // 일치하는 정보가 없으면 null 반환
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