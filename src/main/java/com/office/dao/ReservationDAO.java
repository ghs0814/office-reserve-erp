package com.office.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.office.dto.ReservationDTO;
import com.office.util.DBConnection;

public class ReservationDAO {

    // 1. 새로운 예약 정보 저장
    public synchronized boolean insertReservation(ReservationDTO dto) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        // ROOM_ID로 컬럼명 통일
        String sql = "INSERT INTO RESERVATION (RES_NO, EMP_NO, ROOM_ID, RES_DATE, START_TIME, END_TIME, PURPOSE, STATUS) "
                   + "VALUES (SEQ_RESERVATION.NEXTVAL, ?, ?, ?, ?, ?, ?, '예약완료')";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) { 
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, dto.getEmpNo());
                pstmt.setString(2, dto.getRoomId());
                pstmt.setDate(3, (java.sql.Date) dto.getResDate());
                pstmt.setString(4, dto.getStartTime());
                pstmt.setString(5, dto.getEndTime());
                pstmt.setString(6, dto.getPurpose());

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

    // 2. 예약 중복 확인
    public boolean checkDuplicate(String roomId, java.sql.Date date, String startTime) {
        boolean isDuplicate = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT COUNT(*) FROM RESERVATION WHERE ROOM_ID = ? AND RES_DATE = ? AND START_TIME = ? AND STATUS = '예약완료'";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, roomId);
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
                    dto.setRoomId(rs.getString("ROOM_ID"));
                    dto.setResDate(rs.getDate("RES_DATE"));
                    dto.setStartTime(rs.getString("START_TIME"));
                    dto.setEndTime(rs.getString("END_TIME"));
                    dto.setPurpose(rs.getString("PURPOSE")); // 누락되었던 부분 추가
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

    // 4. 예약 취소 (상태값 변경)
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

    // 5. AJAX용 예약된 시간 목록 가져오기
    public List<String> getReservedTimes(String roomId, String resDate) {
        List<String> reservedTimes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT START_TIME FROM RESERVATION WHERE ROOM_ID = ? AND RES_DATE = ? AND STATUS = '예약완료'";

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomId);
            pstmt.setString(2, resDate);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                reservedTimes.add(rs.getString("START_TIME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, pstmt, rs); // 자원 해제 추가
        }
        return reservedTimes;
    }

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