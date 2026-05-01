package com.office.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.office.dto.EquipmentDTO;
import com.office.util.DBConnection;

/**
 * 비품 재고 관리 및 조회를 담당하는 데이터 접근 객체입니다.
 */
public class EquipmentDAO {

	/**
	 * 전체 비품 목록 조회: 모든 비품을 번호 순서대로 가져와 리스트로 반환합니다.
	 */
	public List<EquipmentDTO> getAllEquipments() {
		List<EquipmentDTO> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

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

	/**
	 * 신규 비품 등록: 현재 비품 번호 중 최대값에 +1을 하여 자동으로 새 번호를 부여하고 등록합니다.
	 */
	public boolean insertEquipment(EquipmentDTO dto) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;

		// NVL과 MAX를 활용하여 수동으로 시퀀스 효과를 낸 쿼리
		String sql = "INSERT INTO EQUIPMENT (EQ_NO, EQ_NAME, TOTAL_COUNT, REMAIN_COUNT) "
				+ "VALUES ((SELECT NVL(MAX(EQ_NO), 0) + 1 FROM EQUIPMENT), ?, ?, ?)";

		try {
			conn = DBConnection.getConnection();
			if (conn != null) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dto.getEqName());
				pstmt.setInt(2, dto.getTotalCount());
				// 신규 등록 시 총 수량과 잔여 수량은 동일하게 세팅합니다.
				pstmt.setInt(3, dto.getTotalCount());

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
	 * 비품 정보 수정: 관리자가 수정한 비품명, 총 수량, 잔여 수량을 업데이트합니다.
	 */
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
	 * 비품 폐기: 더 이상 사용하지 않는 비품 정보를 DB에서 삭제합니다.
	 */
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
	 * 특정 비품 상세 조회: 대여 폼 등에서 선택한 비품 1개의 상세 정보를 가져올 때 사용합니다.
	 */
    public EquipmentDTO getEquipmentDetail(int eqNo) {
        EquipmentDTO dto = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM EQUIPMENT WHERE EQ_NO = ?";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, eqNo);
                
                rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    dto = new EquipmentDTO();
                    dto.setEqNo(rs.getInt("EQ_NO"));
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

	/**
	 * 자원 해제 공통 메서드입니다.
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