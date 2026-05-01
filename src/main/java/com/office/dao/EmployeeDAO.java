package com.office.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.office.dto.EmployeeDTO;
import com.office.util.DBConnection;

/**
 * 사원 관련 데이터베이스 연동을 담당하는 클래스입니다.
 */
public class EmployeeDAO {

    /**
     * 로그인 체크: 아이디와 비밀번호가 일치하면 해당 사원 정보를 DTO에 담아 반환합니다.
     */
    public EmployeeDTO loginCheck(String loginId, String loginPw) {
        EmployeeDTO dto = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        // 아이디와 비밀번호를 조건으로 사원 정보를 조회하는 쿼리
        String sql = "SELECT * FROM EMPLOYEE WHERE LOGIN_ID = ? AND LOGIN_PW = ?";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, loginId);
                pstmt.setString(2, loginPw);
                
                rs = pstmt.executeQuery();
                // 일치하는 데이터가 있으면 DTO 객체를 생성하여 채웁니다.
                if (rs.next()) {
                    dto = new EmployeeDTO();
                    dto.setEmpNo(rs.getInt("EMP_NO"));
                    dto.setLoginId(rs.getString("LOGIN_ID"));
                    dto.setLoginPw(rs.getString("LOGIN_PW"));
                    dto.setEmpName(rs.getString("EMP_NAME"));
                    // 관리자 권한 확인을 위해 결재 레벨(EMP_LEVEL)을 세팅합니다.
                    dto.setEmpLevel(rs.getInt("EMP_LEVEL"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, rs);
        }
        return dto; // 일치하는 정보가 없으면 null을 반환합니다.
    }

    /**
     * 신규 사원 등록: 회원가입 폼에서 입력받은 데이터를 사원 테이블에 추가합니다.
     */
    public boolean insertEmployee(EmployeeDTO dto) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        // 입력받은 모든 정보(사번, ID, PW, 이름, 레벨)를 삽입하는 쿼리
        String sql = "INSERT INTO EMPLOYEE (EMP_NO, LOGIN_ID, LOGIN_PW, EMP_NAME, EMP_LEVEL) VALUES (?, ?, ?, ?, ?)";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, dto.getEmpNo());
                pstmt.setString(2, dto.getLoginId());
                pstmt.setString(3, dto.getLoginPw());
                pstmt.setString(4, dto.getEmpName());
                pstmt.setInt(5, dto.getEmpLevel()); // 선택한 권한 레벨을 세팅합니다.

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
     * 데이터베이스 자원 해제를 위한 공통 메서드입니다.
     */
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