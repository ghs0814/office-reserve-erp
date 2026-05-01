package com.office.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.office.dto.ReservationDTO;
import com.office.util.DBConnection;

/**
 * 회의실 예약 데이터 처리를 담당하는 DAO입니다.
 */
public class ReservationDAO {

    // 1. 새로운 예약 정보 저장 (동시 예약 방지를 위해 synchronized 적용)[cite: 38]
    public synchronized boolean insertReservation(ReservationDTO dto) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        
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

    // 2. 예약 중복 확인 (특정 방/날짜/시간에 완료된 예약이 있는지 체크)[cite: 38]
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

    // 3. 내 예약 내역 조회 (최신 예약 순으로 정렬)[cite: 38]
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
                    dto.setPurpose(rs.getString("PURPOSE"));
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

    // 4. 예약 취소 (물리 삭제 대신 상태값을 '취소됨'으로 변경)[cite: 38]
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

    // 5. 실시간 예약 현황 확인 (특정 방/날짜의 예약된 시간 목록 추출 - AJAX 연동용)[cite: 38]
    public List<String> getReservedTimes(String roomId, String resDate) {
        List<String> reservedTimes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // '예약완료' 상태인 시간대만 가져옵니다.[cite: 38]
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
            closeResource(conn, pstmt, rs);
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