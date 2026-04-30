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