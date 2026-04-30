package com.office.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.office.dto.RoomDTO;
import com.office.util.DBConnection;

public class RoomDAO {

    // 특정 방 번호(roomCode)를 넘겨주면, 그 방의 상세 정보를 DB에서 찾아 DTO로 돌려주는 메서드
    public RoomDTO getRoomDetail(String roomCode) {
        RoomDTO dto = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM ROOM WHERE ROOM_CODE = ?";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, roomCode);
                rs = pstmt.executeQuery();

                // 결과가 있다면 DTO 객체에 포장하기
                if (rs.next()) {
                    dto = new RoomDTO();
                    dto.setRoomId(rs.getString("ROOM_CODE"));
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