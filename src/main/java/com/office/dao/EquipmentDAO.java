package com.office.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.office.dto.EquipmentDTO;
import com.office.util.DBConnection;

public class EquipmentDAO {

	// 1. 전체 비품 목록 조회
	public List<EquipmentDTO> getAllEquipments() {
		List<EquipmentDTO> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// 비품 번호 오름차순으로 정렬하여 가져옵니다.
		String sql = "SELECT * FROM EQUIPMENT ORDER BY EQ_NO ASC";

		try {
			conn = DBConnection.getConnection();
			if (conn != null) {
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					EquipmentDTO dto = new EquipmentDTO();
					dto.setEqNo(rs.getInt("EQ_NO"));
					dto.setEqName(rs.getString("EQ_NAME"));
					dto.setTotalCount(rs.getInt("TOTAL_COUNT"));
					dto.setRemainCount(rs.getInt("REMAIN_COUNT"));

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

	// 1. 신규 비품 등록 (새로운 번호 자동 부여)
	public boolean insertEquipment(EquipmentDTO dto) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;

		// 가장 큰 비품 번호를 찾아 +1을 해주는 방식으로 새 번호를 자동 부여합니다.
		String sql = "INSERT INTO EQUIPMENT (EQ_NO, EQ_NAME, TOTAL_COUNT, REMAIN_COUNT) "
				+ "VALUES ((SELECT NVL(MAX(EQ_NO), 0) + 1 FROM EQUIPMENT), ?, ?, ?)";

		try {
			conn = DBConnection.getConnection();
			if (conn != null) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dto.getEqName());
				pstmt.setInt(2, dto.getTotalCount());
				// 신규 등록 시 총 수량과 잔여 수량은 동일합니다.
				pstmt.setInt(3, dto.getTotalCount());

				int count = pstmt.executeUpdate();
				if (count > 0)
					result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, pstmt, null);
		}
		return result;
	}

	// 2. 비품 정보 수정
	public boolean updateEquipment(EquipmentDTO dto) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "UPDATE EQUIPMENT SET EQ_NAME = ?, TOTAL_COUNT = ?, REMAIN_COUNT = ? WHERE EQ_NO = ?";

		try {
			conn = DBConnection.getConnection();
			if (conn != null) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dto.getEqName());
				pstmt.setInt(2, dto.getTotalCount());
				pstmt.setInt(3, dto.getRemainCount());
				pstmt.setInt(4, dto.getEqNo());

				int count = pstmt.executeUpdate();
				if (count > 0)
					result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, pstmt, null);
		}
		return result;
	}

	// 3. 비품 폐기 (삭제)
	public boolean deleteEquipment(int eqNo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "DELETE FROM EQUIPMENT WHERE EQ_NO = ?";

		try {
			conn = DBConnection.getConnection();
			if (conn != null) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, eqNo);

				int count = pstmt.executeUpdate();
				if (count > 0)
					result = true;
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
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}