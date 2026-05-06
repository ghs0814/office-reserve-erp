package com.office.dao;

import java.sql.*;
import java.util.*;
import java.time.*;
import com.office.dto.LeaveHistoryDTO; // 별도 생성 필요
import com.office.util.DBConnection;

public class LeaveDAO {

	// 내 휴가 신청 내역만 조회하는 메서드 추가
	public List<com.office.dto.LeaveHistoryDTO> getMyLeaveList(int empNo) {
		List<com.office.dto.LeaveHistoryDTO> list = new ArrayList<>();
		String sql = "SELECT * FROM LEAVE_HISTORY WHERE EMP_NO = ? ORDER BY LEAVE_NO DESC";

		try (Connection conn = com.office.util.DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, empNo);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				com.office.dto.LeaveHistoryDTO dto = new com.office.dto.LeaveHistoryDTO();
				dto.setLeaveNo(rs.getInt("LEAVE_NO"));
				dto.setEmpNo(rs.getInt("EMP_NO"));
				dto.setStartDate(rs.getDate("START_DATE"));
				dto.setEndDate(rs.getDate("END_DATE"));
				dto.setUseDays(rs.getInt("USE_DAYS"));
				dto.setReason(rs.getString("REASON"));
				dto.setStatus(rs.getString("STATUS"));
				dto.setApprovalStep(rs.getInt("APPROVAL_STEP"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 평일(휴가 사용일) 계산 로직
	public int calculateWorkingDays(LocalDate start, LocalDate end) {
		int workingDays = 0;
		LocalDate date = start;
		while (!date.isAfter(end)) {
			DayOfWeek day = date.getDayOfWeek();
			if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
				workingDays++;
			}
			date = date.plusDays(1);
		}
		return workingDays;
	}

	public boolean insertLeave(LeaveHistoryDTO dto) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtUpdateEmp = null; // ★ 연차 차감용

		// 도장(SIGN) 정보까지 모두 저장하도록 쿼리를 확장합니다.
		String sql = "INSERT INTO LEAVE_HISTORY (LEAVE_NO, EMP_NO, START_DATE, END_DATE, USE_DAYS, REASON, STATUS, APPROVAL_STEP, "
				+ "SIGN1, SIGN1_DATE, SIGN2, SIGN2_DATE, SIGN3, SIGN3_DATE, SIGN4, SIGN4_DATE, SIGN5, SIGN5_DATE) "
				+ "VALUES (SEQ_LEAVE.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false); // ★ 트랜잭션 시작

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getEmpNo());
			pstmt.setDate(2, dto.getStartDate());
			pstmt.setDate(3, dto.getEndDate());
			pstmt.setInt(4, dto.getUseDays());
			pstmt.setString(5, dto.getReason());
			pstmt.setString(6, dto.getStatus());
			pstmt.setInt(7, dto.getApprovalStep());
			// 컨트롤러에서 세팅한 자동 도장 데이터 바인딩
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
				// ★ 최고 등급 직원 신청 시(상태가 승인완료인 경우) 즉시 연차 차감
				if ("승인완료".equals(dto.getStatus())) {
					String updateSql = "UPDATE EMPLOYEE SET CUR_LEAVE = CUR_LEAVE - ? WHERE EMP_NO = ?";
					pstmtUpdateEmp = conn.prepareStatement(updateSql);
					pstmtUpdateEmp.setInt(1, dto.getUseDays());
					pstmtUpdateEmp.setInt(2, dto.getEmpNo());

					if (pstmtUpdateEmp.executeUpdate() > 0) {
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
			try {
				if (conn != null)
					conn.rollback();
			} catch (Exception ex) {
			}
			e.printStackTrace();
		} finally {
			try {
				if (pstmtUpdateEmp != null)
					pstmtUpdateEmp.close();
			} catch (Exception e) {
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return result;
	}

	public boolean processLeaveApproval(int leaveNo, int step, String managerName, boolean isApprove) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtUpdateLeave = null;

		// 결재 단계에 따른 컬럼명 동적 생성
		String signCol = "SIGN" + step;
		String dateCol = "SIGN" + step + "_DATE";

		// 승인 시: 마지막 5단계면 '승인완료', 아니면 '승인대기' 유지
		String status = isApprove ? (step == 5 ? "승인완료" : "승인대기") : "반려됨";
		int nextStep = isApprove ? (step < 5 ? step + 1 : 5) : step;

		String sql = "UPDATE LEAVE_HISTORY SET " + signCol + " = ?, " + dateCol
				+ " = SYSDATE, STATUS = ?, APPROVAL_STEP = ? WHERE LEAVE_NO = ?";

		try {
			conn = com.office.util.DBConnection.getConnection();
			conn.setAutoCommit(false); // 트랜잭션 시작

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, managerName);
			pstmt.setString(2, status);
			pstmt.setInt(3, nextStep);
			pstmt.setInt(4, leaveNo);

			int count = pstmt.executeUpdate();

			// ★ 최종 5단계 승인 시에만 실제 연차 차감 로직 실행
			if (count > 0 && isApprove && step == 5) {
				String updateLeaveSql = "UPDATE EMPLOYEE SET CUR_LEAVE = CUR_LEAVE - "
						+ "(SELECT USE_DAYS FROM LEAVE_HISTORY WHERE LEAVE_NO = ?) "
						+ "WHERE EMP_NO = (SELECT EMP_NO FROM LEAVE_HISTORY WHERE LEAVE_NO = ?)";

				pstmtUpdateLeave = conn.prepareStatement(updateLeaveSql);
				pstmtUpdateLeave.setInt(1, leaveNo);
				pstmtUpdateLeave.setInt(2, leaveNo);

				if (pstmtUpdateLeave.executeUpdate() > 0) {
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
			try {
				if (conn != null)
					conn.rollback();
			} catch (Exception ex) {
			}
			e.printStackTrace();
		} finally {
			// 자원 해제 로직 (pstmt, conn 등 close)
		}
		return result;
	}

	// 결재 대기 목록 조회 (관리자용)
	public List<com.office.dto.LeaveHistoryDTO> getPendingLeaveList(int managerLevel) {
		List<com.office.dto.LeaveHistoryDTO> list = new ArrayList<>();
		String sql = "SELECT h.*, e.EMP_NAME, e.DEPT FROM LEAVE_HISTORY h " + "JOIN EMPLOYEE e ON h.EMP_NO = e.EMP_NO "
				+ "WHERE h.STATUS = '승인대기' AND h.APPROVAL_STEP = ? ORDER BY h.START_DATE ASC";
		try (Connection conn = com.office.util.DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, managerLevel);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				com.office.dto.LeaveHistoryDTO dto = new com.office.dto.LeaveHistoryDTO();
				dto.setLeaveNo(rs.getInt("LEAVE_NO"));
				dto.setEmpNo(rs.getInt("EMP_NO"));
				dto.setEmpName(rs.getString("EMP_NAME"));
				dto.setDept(rs.getString("DEPT"));
				dto.setStartDate(rs.getDate("START_DATE"));
				dto.setEndDate(rs.getDate("END_DATE"));
				dto.setUseDays(rs.getInt("USE_DAYS"));
				dto.setReason(rs.getString("REASON"));
				dto.setApprovalStep(rs.getInt("APPROVAL_STEP"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 특정 휴가 신청 상세 조회 (기존 코드 아래에 추가)
	public com.office.dto.LeaveHistoryDTO getLeaveDetail(int leaveNo) {
		com.office.dto.LeaveHistoryDTO dto = null;
		String sql = "SELECT h.*, e.EMP_NAME, e.EMP_LEVEL, e.DEPT " + "FROM LEAVE_HISTORY h "
				+ "JOIN EMPLOYEE e ON h.EMP_NO = e.EMP_NO " + "WHERE h.LEAVE_NO = ?";

		try (Connection conn = com.office.util.DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, leaveNo);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new com.office.dto.LeaveHistoryDTO();
				dto.setLeaveNo(rs.getInt("LEAVE_NO"));
				dto.setEmpNo(rs.getInt("EMP_NO"));
				dto.setEmpName(rs.getString("EMP_NAME"));
				dto.setDept(rs.getString("DEPT"));
				dto.setStartDate(rs.getDate("START_DATE"));
				dto.setEndDate(rs.getDate("END_DATE"));
				dto.setUseDays(rs.getInt("USE_DAYS"));
				dto.setReason(rs.getString("REASON"));
				dto.setStatus(rs.getString("STATUS"));
				dto.setApprovalStep(rs.getInt("APPROVAL_STEP"));
				// 서명 정보 세팅
				dto.setSign1(rs.getString("SIGN1"));
				dto.setSign1Date(rs.getDate("SIGN1_DATE"));
				dto.setSign2(rs.getString("SIGN2"));
				dto.setSign2Date(rs.getDate("SIGN2_DATE"));
				dto.setSign3(rs.getString("SIGN3"));
				dto.setSign3Date(rs.getDate("SIGN3_DATE"));
				dto.setSign4(rs.getString("SIGN4"));
				dto.setSign4Date(rs.getDate("SIGN4_DATE"));
				dto.setSign5(rs.getString("SIGN5"));
				dto.setSign5Date(rs.getDate("SIGN5_DATE"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	// 기존 코드 하단에 추가
	public List<com.office.dto.LeaveHistoryDTO> getAllLeaveDocuments() {
		List<com.office.dto.LeaveHistoryDTO> list = new java.util.ArrayList<>();
		// SQL JOIN을 통해 e.EMP_LEVEL을 가져옵니다.
		String sql = "SELECT h.*, e.EMP_NAME, e.EMP_LEVEL, e.DEPT FROM LEAVE_HISTORY h "
				+ "JOIN EMPLOYEE e ON h.EMP_NO = e.EMP_NO ORDER BY h.LEAVE_NO DESC";
		try (java.sql.Connection conn = com.office.util.DBConnection.getConnection();
				java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
				java.sql.ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				com.office.dto.LeaveHistoryDTO dto = new com.office.dto.LeaveHistoryDTO();
				dto.setLeaveNo(rs.getInt("LEAVE_NO"));
				dto.setEmpNo(rs.getInt("EMP_NO"));
				dto.setEmpName(rs.getString("EMP_NAME"));
				dto.setDept(rs.getString("DEPT"));
				dto.setStartDate(rs.getDate("START_DATE"));
				dto.setEndDate(rs.getDate("END_DATE"));
				dto.setUseDays(rs.getInt("USE_DAYS"));
				dto.setStatus(rs.getString("STATUS"));
				dto.setApprovalStep(rs.getInt("APPROVAL_STEP"));
				dto.setReason(rs.getString("REASON"));
				dto.setEmpLevel(rs.getInt("EMP_LEVEL")); // ★ 추가: 직급 매핑
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 상세 화면용 승인 (POST 방식 대응)
	public boolean processApproval(int leaveNo, int step, String managerName, boolean isApprove) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtUpdateEmp = null;

		String signCol = "SIGN" + step;
		String dateCol = "SIGN" + step + "_DATE";
		String status = isApprove ? "승인대기" : "반려됨";
		int nextStep = isApprove ? step + 1 : step;

		if (isApprove && step == 5)
			status = "승인완료";

		String sql = "UPDATE LEAVE_HISTORY SET " + signCol + " = ?, " + dateCol
				+ " = SYSDATE, STATUS = ?, APPROVAL_STEP = ? WHERE LEAVE_NO = ?";

		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, managerName);
			pstmt.setString(2, status);
			pstmt.setInt(3, nextStep);
			pstmt.setInt(4, leaveNo);
			int count = pstmt.executeUpdate();

			if (count > 0 && isApprove && step == 5) {
				String updateSql = "UPDATE EMPLOYEE SET CUR_LEAVE = CUR_LEAVE - (SELECT USE_DAYS FROM LEAVE_HISTORY WHERE LEAVE_NO = ?) WHERE EMP_NO = (SELECT EMP_NO FROM LEAVE_HISTORY WHERE LEAVE_NO = ?)";
				pstmtUpdateEmp = conn.prepareStatement(updateSql);
				pstmtUpdateEmp.setInt(1, leaveNo);
				pstmtUpdateEmp.setInt(2, leaveNo);
				if (pstmtUpdateEmp.executeUpdate() > 0) {
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
			try {
				if (conn != null)
					conn.rollback();
			} catch (Exception ex) {
			}
			e.printStackTrace();
		} finally {
			closeResource(conn, pstmt, null);
		}
		return result;
	}

	// 퀵 승인용 (GET 방식 대응)
	public boolean processStepApproval(int leaveNo, int currentStep, String managerName, String action) {
		boolean isApprove = "approve".equals(action);
		return processApproval(leaveNo, currentStep, managerName, isApprove);
	}

	private void closeResource(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
		}
	}
}