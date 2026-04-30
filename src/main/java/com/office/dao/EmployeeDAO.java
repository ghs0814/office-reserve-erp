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
                 // [중요] DB에서 EMP_LEVEL 컬럼 값을 가져와 DTO에 세팅합니다.
                    dto.setEmpLevel(rs.getInt("EMP_LEVEL"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, rs);
        }
        return dto; // 일치하는 정보가 없으면 null 반환
    }
 // EmployeeDAO.java 안의 insertEmployee 메서드를 통째로 아래 코드로 교체합니다.

    public boolean insertEmployee(EmployeeDTO dto) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        // EMP_LEVEL에 고정값 1 대신 물음표(?)를 사용합니다.
        String sql = "INSERT INTO EMPLOYEE (EMP_NO, LOGIN_ID, LOGIN_PW, EMP_NAME, EMP_LEVEL) VALUES (?, ?, ?, ?, ?)";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, dto.getEmpNo());
                pstmt.setString(2, dto.getLoginId());
                pstmt.setString(3, dto.getLoginPw());
                pstmt.setString(4, dto.getEmpName());
                pstmt.setInt(5, dto.getEmpLevel()); // 폼에서 받아온 레벨 값을 세팅합니다.

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