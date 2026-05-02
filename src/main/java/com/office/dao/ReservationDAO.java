package com.office.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.office.dto.ReservationDTO;
import com.office.util.DBConnection;

public class ReservationDAO {

    // ★ [신규] 유령 예약 청소 메서드 (조회하기 직전마다 이 메서드를 호출해서 만료된 임시 데이터를 지웁니다)
    public void cleanUpGhostReservations() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        // 상태가 '임시선점'이면서 현재 시간(SYSDATE)이 만료 시간(EXPIRE_TIME)을 넘긴 쓰레기 데이터를 삭제합니다.
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

    // ★ [신규] 시간 버튼 클릭 시 5분 타이머와 함께 '임시선점' 데이터 삽입 (AJAX 용도)
    public synchronized int holdReservation(ReservationDTO dto) {
        // 넣기 전에 유령 데이터 청소
        cleanUpGhostReservations();

        // 1. 혹시 누군가 그 찰나에 먼저 선점했는지 확인
        if(checkDuplicate(dto.getRoomId(), (java.sql.Date)dto.getResDate(), dto.getStartTime())) {
            return -1; // -1: 이미 선점당함
        }

        int newResNo = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 상태는 '임시선점', EXPIRE_TIME은 현재시간(SYSDATE) + 5분(5/24/60)
        String sql = "INSERT INTO RESERVATION (RES_NO, EMP_NO, ROOM_ID, RES_DATE, START_TIME, END_TIME, STATUS, EXPIRE_TIME) "
                   + "VALUES (SEQ_RESERVATION.NEXTVAL, ?, ?, ?, ?, ?, '임시선점', SYSDATE + (5/24/60))";
        
     // 기존 쿼리: SYSDATE + (5 / (24 * 60))
//        String sql = "INSERT INTO RESERVATION (RES_NO, EMP_NO, ROOM_ID, RES_DATE, START_TIME, END_TIME, STATUS, EXPIRE_TIME) "
//                   + "VALUES (SEQ_RESERVATION.NEXTVAL, ?, ?, ?, ?, ?, '임시선점', SYSDATE + (10 / (24 * 60 * 60)))"; 
                   // 테스트용: 10초 뒤 만료
        
        // 방금 넣은 예약번호(RES_NO)를 리턴받기 위한 쿼리
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
                        newResNo = rs.getInt(1); // 방금 생성된 임시 예약번호
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); } 
        finally { closeResource(conn, pstmt, rs); }
        
        return newResNo;
    }

    // ★ [수정] 확인 버튼을 눌렀을 때 '임시선점' -> '예약완료'로 업데이트
    public boolean confirmReservation(int resNo, String purpose) {
        // 확정하기 전에 유령 데이터 청소
        cleanUpGhostReservations();

        boolean result = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        // 5분이 지나기 전에 확정 요청이 들어온 경우 상태를 예약완료로 변경하고 만료시간은 NULL 처리
        String sql = "UPDATE RESERVATION SET STATUS = '예약완료', PURPOSE = ?, EXPIRE_TIME = NULL "
                   + "WHERE RES_NO = ? AND STATUS = '임시선점'";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, purpose);
                pstmt.setInt(2, resNo);
                
                int count = pstmt.executeUpdate();
                if (count > 0) result = true; // 성공적으로 확정됨
            }
        } catch (Exception e) { e.printStackTrace(); } 
        finally { closeResource(conn, pstmt, null); }
        return result;
    }

    // 기존 2. 예약 중복 확인 (예약완료 + 임시선점 모두 막아야 함)
    public boolean checkDuplicate(String roomId, java.sql.Date date, String startTime) {
        boolean isDuplicate = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        // 예약완료 상태이거나, 다른 사람이 아직 5분 임시선점 중인 경우 모두 차단
        String sql = "SELECT COUNT(*) FROM RESERVATION WHERE ROOM_ID = ? AND RES_DATE = ? AND START_TIME = ? AND STATUS IN ('예약완료', '임시선점')";

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
        } catch (Exception e) { e.printStackTrace(); } 
        finally { closeResource(conn, pstmt, rs); }
        return isDuplicate;
    }

    // 기존 3. 내 예약 내역 조회
    public List<ReservationDTO> getMyReservations(int empNo) {
        cleanUpGhostReservations(); // 조회 전 찌꺼기 청소
        
        List<ReservationDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        // 내 목록에는 확정된 예약과 취소된 예약만 보여줍니다. (임시선점 상태는 숨김)
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

    // 기존 4. 예약 취소 
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

    // 기존 5. 실시간 예약 현황 확인 (AJAX 연동용)
    public List<String> getReservedTimes(String roomId, String resDate) {
        cleanUpGhostReservations(); // 누군가 조회할 때마다 찌꺼기 청소 실행!
        
        List<String> reservedTimes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 예약완료 또는 다른 사람이 임시선점 중인 시간을 모두 가져와서 회색 버튼으로 처리합니다.
        String sql = "SELECT START_TIME FROM RESERVATION WHERE ROOM_ID = ? AND RES_DATE = ? AND STATUS IN ('예약완료', '임시선점')";

        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomId);
            pstmt.setString(2, resDate);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                reservedTimes.add(rs.getString("START_TIME"));
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