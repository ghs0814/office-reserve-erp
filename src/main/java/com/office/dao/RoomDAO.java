package com.office.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.office.dto.RoomDTO;
import com.office.util.DBConnection;

public class RoomDAO {

    // 특정 방 번호(roomId)를 넘겨주면, 그 방의 상세 정보를 DB에서 찾아 DTO로 돌려주는 메서드
    public RoomDTO getRoomDetail(String roomId) {
        RoomDTO dto = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // ROOM_CODE를 ROOM_ID로 올바르게 수정했습니다.
        String sql = "SELECT * FROM ROOM WHERE ROOM_ID = ?";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, roomId);
                rs = pstmt.executeQuery();

                // 결과가 있다면 DTO 객체에 포장하기
                if (rs.next()) {
                    dto = new RoomDTO();
                    // 여기도 DB 컬럼명에 맞춰 ROOM_ID로 수정했습니다.
                    dto.setRoomId(rs.getString("ROOM_ID"));
                    dto.setRoomName(rs.getString("ROOM_NAME"));
                    dto.setCapacity(rs.getInt("CAPACITY"));
                    dto.setHasBeam(rs.getString("HAS_BEAM"));
                    dto.setDescription(rs.getString("DESCRIPTION"));
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
        return dto;
    }
}