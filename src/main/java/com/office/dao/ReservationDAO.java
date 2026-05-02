package com.office.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.office.dto.ReservationDTO;
import com.office.util.DBConnection;

public class ReservationDAO {

    // 1. [유지] 유령 예약 청소 메서드
    public void cleanUpGhostReservations() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "DELETE FROM RESERVATION WHERE STATUS = '임시선점' AND EXPIRE_TIME < SYSDATE";
        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.executeUpdate();
            }
        } catch (Exception e) { e.printStackTrace(); } 
        finally { closeResource(conn, pstmt, null); }
    }

    // 2. [수정됨] 시간 버튼 클릭 시 5분 타이머와 함께 '임시선점' 데이터 삽입 (AJAX 용도)
    public synchronized int holdReservation(ReservationDTO dto) {
        cleanUpGhostReservations();

        // ★ 변경점: 이제 시작시간과 종료시간 전체 범위가 겹치는지 검사합니다.
        if(checkDuplicate(dto.getRoomId(), (java.sql.Date)dto.getResDate(), dto.getStartTime(), dto.getEndTime())) {
            return -1; // -1: 범위 내에 이미 예약된 시간이 있음
        }

        int newResNo = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO RESERVATION (RES_NO, EMP_NO, ROOM_ID, RES_DATE, START_TIME, END_TIME, STATUS, EXPIRE_TIME) "
                   + "VALUES (SEQ_RESERVATION.NEXTVAL, ?, ?, ?, ?, ?, '임시선점', SYSDATE + (5/24/60))";
        
        String[] generatedColumns = {"RES_NO"};

        try {
            conn = DBConnection.getConnection();
            if (conn != null) { 
                pstmt = conn.prepareStatement(sql, generatedColumns);
                pstmt.setInt(1, dto.getEmpNo());
                pstmt.setString(2, dto.getRoomId());
                pstmt.setDate(3, (java.sql.Date) dto.getResDate());
                pstmt.setString(4, dto.getStartTime());
                pstmt.setString(5, dto.getEndTime());

                int count = pstmt.executeUpdate();
                if (count > 0) {
                    rs = pstmt.getGeneratedKeys();
                    if(rs.next()) {
                        newResNo = rs.getInt(1); 
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); } 
        finally { closeResource(conn, pstmt, rs); }
        
        return newResNo;
    }

    // 3. [유지] 확인 버튼을 눌렀을 때 '임시선점' -> '예약완료'로 업데이트
    public boolean confirmReservation(int resNo, String purpose) {
        cleanUpGhostReservations();

        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        String sql = "UPDATE RESERVATION SET STATUS = '예약완료', PURPOSE = ?, EXPIRE_TIME = NULL "
                   + "WHERE RES_NO = ? AND STATUS = '임시선점'";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, purpose);
                pstmt.setInt(2, resNo);
                
                int count = pstmt.executeUpdate();
                if (count > 0) result = true; 
            }
        } catch (Exception e) { e.printStackTrace(); } 
        finally { closeResource(conn, pstmt, null); }
        return result;
    }

    // 4. [완전 교체] 범위 중복(Overlap) 검사 로직
    // 내가 신청한 (reqStart ~ reqEnd) 범위 안에 기존 예약이 하나라도 겹치면 true 반환
    public boolean checkDuplicate(String roomId, java.sql.Date date, String reqStart, String reqEnd) {
        boolean isDuplicate = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        // ★ 핵심: 두 시간 구간이 겹치려면 (기존 시작 < 내 종료) AND (기존 종료 > 내 시작) 이어야 합니다.
        String sql = "SELECT COUNT(*) FROM RESERVATION "
                   + "WHERE ROOM_ID = ? AND RES_DATE = ? AND STATUS IN ('예약완료', '임시선점') "
                   + "AND START_TIME < ? AND END_TIME > ?";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, roomId);
                pstmt.setDate(2, date);
                pstmt.setString(3, reqEnd);   // 기존 예약의 START_TIME < 나의 END_TIME
                pstmt.setString(4, reqStart); // 기존 예약의 END_TIME > 나의 START_TIME

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    if (rs.getInt(1) > 0) isDuplicate = true; // 1개라도 겹치면 중복!
                }
            }
        } catch (Exception e) { e.printStackTrace(); } 
        finally { closeResource(conn, pstmt, rs); }
        return isDuplicate;
    }

    // 5. [유지] 내 예약 내역 조회
    public List<ReservationDTO> getMyReservations(int empNo) {
        cleanUpGhostReservations(); 
        
        List<ReservationDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT * FROM RESERVATION WHERE EMP_NO = ? AND STATUS != '임시선점' ORDER BY RES_DATE DESC, START_TIME DESC";

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
        } catch (Exception e) { e.printStackTrace(); } 
        finally { closeResource(conn, pstmt, rs); }
        return list;
    }

    // 6. [유지] 예약 취소 
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
        } catch (Exception e) { e.printStackTrace(); } 
        finally { closeResource(conn, pstmt, null); }
        return result;
    }

    // 7. [완전 교체] 실시간 예약 현황 확인 (시간 쪼개기 로직 추가)
    // DB에 "09:00~12:00" 1줄이 있어도, 화면에는 ["09:00", "10:00", "11:00"] 로 쪼개서 줍니다.
    public List<String> getReservedTimes(String roomId, String resDate) {
        cleanUpGhostReservations(); 
        
        List<String> reservedTimes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT START_TIME, END_TIME FROM RESERVATION WHERE ROOM_ID = ? AND RES_DATE = ? AND STATUS IN ('예약완료', '임시선점')";

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomId);
            pstmt.setString(2, resDate);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String start = rs.getString("START_TIME");
                String end = rs.getString("END_TIME");
                
                int startHour = Integer.parseInt(start.split(":")[0]);
                int endHour = Integer.parseInt(end.split(":")[0]);
                
                // 09시부터 12시라면 -> 9, 10, 11 을 리스트에 추가합니다.
                for (int i = startHour; i < endHour; i++) {
                    String timeStr = (i < 10 ? "0" + i : i) + ":00";
                    if (!reservedTimes.contains(timeStr)) {
                        reservedTimes.add(timeStr);
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); } 
        finally { closeResource(conn, pstmt, rs); }
        return reservedTimes;
    }

    private void closeResource(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}