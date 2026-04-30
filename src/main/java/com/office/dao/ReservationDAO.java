package com.office.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.office.dto.ReservationDTO;
import com.office.util.DBConnection;

public class ReservationDAO {

    // 1. 새로운 예약 정보 저장 (동시성 제어를 위해 synchronized 적용)
    public synchronized boolean insertReservation(ReservationDTO dto) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        // 시퀀스(SEQ_RESERVATION)를 사용하여 예약 번호를 자동 부여한다고 가정
        String sql = "INSERT INTO RESERVATION (RES_NO, EMP_NO, ROOM_CODE, RES_DATE, START_TIME, END_TIME, STATUS) "
                   + "VALUES (SEQ_RESERVATION.NEXTVAL, ?, ?, ?, ?, ?, '예약완료')";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) { // DB 연결이 성공했을 때만 실행
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, dto.getEmpNo());
                pstmt.setString(2, dto.getRoomId());
                pstmt.setDate(3, dto.getResDate());
                pstmt.setString(4, dto.getStartTime());
                pstmt.setString(5, dto.getEndTime());

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

    // 2. 예약 중복 확인 (해당 시간에 예약이 있으면 true 반환)
    public boolean checkDuplicate(String roomCode, java.sql.Date date, String startTime) {
        boolean isDuplicate = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT COUNT(*) FROM RESERVATION WHERE ROOM_CODE = ? AND RES_DATE = ? AND START_TIME = ? AND STATUS = '예약완료'";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, roomCode);
                pstmt.setDate(2, date);
                pstmt.setString(3, startTime);

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    if (rs.getInt(1) > 0) isDuplicate = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, rs);
        }
        return isDuplicate;
    }

    // 3. 마이페이지용 내 예약 내역 조회
    public List<ReservationDTO> getMyReservations(int empNo) {
        List<ReservationDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT * FROM RESERVATION WHERE EMP_NO = ? ORDER BY RES_DATE DESC, START_TIME DESC";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, empNo);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    ReservationDTO dto = new ReservationDTO();
                    dto.setResNo(rs.getInt("RES_NO"));
                    dto.setEmpNo(rs.getInt("EMP_NO"));
                    dto.setRoomId(rs.getString("ROOM_CODE"));
                    dto.setResDate(rs.getDate("RES_DATE"));
                    dto.setStartTime(rs.getString("START_TIME"));
                    dto.setEndTime(rs.getString("END_TIME"));
                    dto.setStatus(rs.getString("STATUS"));
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

    // 4. 예약 취소 (데이터를 지우지 않고 상태값만 '취소됨'으로 변경)
    public boolean cancelReservation(int resNo) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        String sql = "UPDATE RESERVATION SET STATUS = '취소됨' WHERE RES_NO = ?";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, resNo);
                
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

    // 자원 해제 공통 메서드 (중복 코드 방지)
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