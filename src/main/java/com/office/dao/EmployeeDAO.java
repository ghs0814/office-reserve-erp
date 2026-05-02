package com.office.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.office.dto.EmployeeDTO;
import com.office.util.DBConnection;

/**
 * 사원 관련 데이터베이스 연동을 담당하는 클래스입니다.
 */
public class EmployeeDAO {

	/**
	 * 로그인 체크: 사번(empNo)과 비밀번호(empPw)가 일치하면 사원 정보를 반환합니다. (아이디 대신 사번으로 로그인하도록 수정됨)
	 */
	public EmployeeDTO loginCheck(String loginNo, String loginPw) {
		EmployeeDTO dto = null;
		// LOGIN_ID 대신 EMP_NO를 조회 조건으로 사용합니다.
		String sql = "SELECT emp_no, emp_pw, emp_name, emp_level, manager FROM EMPLOYEE WHERE emp_no = ? AND emp_pw = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, loginNo);
			pstmt.setString(2, loginPw);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					dto = new EmployeeDTO();
					dto.setEmpNo(rs.getInt("emp_no"));
					dto.setEmpPw(rs.getString("emp_pw"));
					dto.setEmpName(rs.getString("emp_name"));
					dto.setEmpLevel(rs.getInt("emp_level"));
					dto.setManager(rs.getString("manager"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	/**
	 * 회원가입(정보 업데이트): 초기 사원 데이터에 사용자가 입력한 비밀번호를 업데이트합니다. (이제 INSERT가 아니라, 이미 존재하는 사원
	 * 정보에 PW만 UPDATE 하는 방식으로 변경됨)
	 */
	public boolean updateEmployeePassword(String empNo, String empPw) {
		boolean result = false;
		// 사번을 조건으로 비밀번호를 업데이트합니다.
		String sql = "UPDATE EMPLOYEE SET emp_pw = ? WHERE emp_no = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, empPw);
			pstmt.setString(2, empNo);

			int count = pstmt.executeUpdate();
			if (count > 0) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 사번으로 사원 정보(이름, 직급, 매니저 여부)를 조회하는 기능 (회원가입 전 정보 확인용)
	 */
	public EmployeeDTO getEmployeeByNo(String empNo) {
		EmployeeDTO dto = null;
		String sql = "SELECT emp_no, emp_name, emp_level, manager FROM EMPLOYEE WHERE emp_no = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, empNo);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					dto = new EmployeeDTO();
					dto.setEmpNo(rs.getInt("emp_no"));
					dto.setEmpName(rs.getString("emp_name"));
					dto.setEmpLevel(rs.getInt("emp_level"));
					dto.setManager(rs.getString("manager"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	// 관리자 페이지용: 전체 사원 목록 조회
	public List<EmployeeDTO> getAllEmployees() {
		List<EmployeeDTO> list = new ArrayList<>();
		// 관리자(Y)가 맨 위에, 그다음 직급 높은 순, 마지막으로 사번 순으로 정렬합니다.
		String sql = "SELECT emp_no, emp_name, emp_level, manager FROM EMPLOYEE ORDER BY manager DESC, emp_level DESC, emp_no ASC";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				EmployeeDTO dto = new EmployeeDTO();
				dto.setEmpNo(rs.getInt("emp_no")); // int 타입 유지
				dto.setEmpName(rs.getString("emp_name"));
				dto.setEmpLevel(rs.getInt("emp_level"));
				dto.setManager(rs.getString("manager"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 1. 사원 직급 변경
	public boolean updateEmployeeLevel(int empNo, int newLevel) {
		boolean result = false;
		String sql = "UPDATE EMPLOYEE SET emp_level = ? WHERE emp_no = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, newLevel);
			pstmt.setInt(2, empNo);

			if (pstmt.executeUpdate() > 0)
				result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 2. 관리자 권한 이양 (기존 관리자는 N, 새 관리자는 Y로 변경)
	public boolean transferManagerRole(int oldManagerNo, int newManagerNo) {
		boolean result = false;
		// 두 개의 쿼리를 실행해야 하므로 수동 커밋 모드를 사용할 수도 있지만,
		// 간단하게 두 번의 UPDATE 문을 순차적으로 실행합니다.
		String sql1 = "UPDATE EMPLOYEE SET manager = 'N' WHERE emp_no = ?";
		String sql2 = "UPDATE EMPLOYEE SET manager = 'Y' WHERE emp_no = ?";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt1 = conn.prepareStatement(sql1);
				PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {

			// 기존 관리자 권한 박탈
			pstmt1.setInt(1, oldManagerNo);
			pstmt1.executeUpdate();

			// 새 관리자 권한 부여
			pstmt2.setInt(1, newManagerNo);
			if (pstmt2.executeUpdate() > 0)
				result = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 사원 비밀번호 변경 메서드
	public boolean updatePassword(int empNo, String newPw) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;

		// 해당 사번(EMP_NO)의 비밀번호를 새 비밀번호로 업데이트합니다.
		String sql = "UPDATE EMPLOYEE SET EMP_PW = ? WHERE EMP_NO = ?";

		try {
			conn = DBConnection.getConnection();
			if (conn != null) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, newPw);
				pstmt.setInt(2, empNo);

				int count = pstmt.executeUpdate();
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// 3. 퇴사 처리 (데이터 영구 삭제)
	// 주의: 실제 실무에서는 DELETE 대신 status='퇴사' 로 UPDATE 하지만, 요청하신 대로 삭제 처리합니다.
	public boolean deleteEmployee(int empNo) {
		boolean result = false;
		String sql = "DELETE FROM EMPLOYEE WHERE emp_no = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, empNo);

			if (pstmt.executeUpdate() > 0)
				result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	// 사번과 이름으로 비밀번호 찾기 메서드
    public String findPassword(int empNo, String empName) {
        String pw = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 사번과 이름이 모두 일치하는 데이터의 비밀번호를 가져옵니다.
        String sql = "SELECT EMP_PW FROM EMPLOYEE WHERE EMP_NO = ? AND EMP_NAME = ?";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, empNo);
                pstmt.setString(2, empName);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    pw = rs.getString("EMP_PW");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pw;
    }
}