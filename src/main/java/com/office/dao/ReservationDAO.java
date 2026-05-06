package com.office.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.office.dto.ReservationDTO;
import com.office.util.DBConnection;

public class ReservationDAO {

    // 1. 유령 예약 청소 메서드
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

    // 2. [완벽 수정됨] 시간 버튼 클릭 시 5분 타이머와 함께 '임시선점' 데이터 삽입 
    public synchronized int holdReservation(ReservationDTO dto) {
        cleanUpGhostReservations();

        if(checkDuplicate(dto.getRoomId(), (java.sql.Date)dto.getResDate(), dto.getStartTime(), dto.getEndTime())) {
            return -1; 
        }

        int newResNo = 0;
        Connection conn = null;
        PreparedStatement pstmtSeq = null;
        PreparedStatement pstmt = null;
        ResultSet rsSeq = null;

        try {
            conn = DBConnection.getConnection();
            if (conn != null) { 
                // ★ 핵심 해결책 1: 오라클 버그를 피하기 위해 시퀀스 번호를 안전하게 직접 뽑아옵니다.
                String seqSql = "SELECT SEQ_RESERVATION.NEXTVAL FROM DUAL";
                pstmtSeq = conn.prepareStatement(seqSql);
                rsSeq = pstmtSeq.executeQuery();
                if (rsSeq.next()) {
                    newResNo = rsSeq.getInt(1); 
                }

                if (newResNo > 0) {
                    // ★ 핵심 해결책 2: 시간 계산 오류 방지를 위해 명확하게 INTERVAL '5' MINUTE 사용
                    String sql = "INSERT INTO RESERVATION (RES_NO, EMP_NO, ROOM_ID, RES_DATE, START_TIME, END_TIME, STATUS, EXPIRE_TIME) "
                               + "VALUES (?, ?, ?, ?, ?, ?, '임시선점', SYSDATE + INTERVAL '5' MINUTE)";
                    
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, newResNo);
                    pstmt.setInt(2, dto.getEmpNo());
                    pstmt.setString(3, dto.getRoomId());
                    pstmt.setDate(4, (java.sql.Date) dto.getResDate());
                    pstmt.setString(5, dto.getStartTime());
                    pstmt.setString(6, dto.getEndTime());

                    int count = pstmt.executeUpdate();
                    if (count == 0) {
                        newResNo = 0; 
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); } 
        finally { 
            if (rsSeq != null) try { rsSeq.close(); } catch(Exception e) {}
            if (pstmtSeq != null) try { pstmtSeq.close(); } catch(Exception e) {}
            closeResource(conn, pstmt, null); 
        }
        
        return newResNo;
    }

    // 3. 확인 버튼을 눌렀을 때 '임시선점' -> '예약완료'로 업데이트
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

    // 4. 범위 중복(Overlap) 검사 로직
    public boolean checkDuplicate(String roomId, java.sql.Date date, String reqStart, String reqEnd) {
        boolean isDuplicate = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT COUNT(*) FROM RESERVATION "
                   + "WHERE ROOM_ID = ? AND RES_DATE = ? AND STATUS IN ('예약완료', '임시선점') "
                   + "AND START_TIME < ? AND END_TIME > ?";

        try {
            conn = DBConnection.getConnection();
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, roomId);
                pstmt.setDate(2, date);
                pstmt.setString(3, reqEnd);   
                pstmt.setString(4, reqStart); 

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    if (rs.getInt(1) > 0) isDuplicate = true; 
                }
            }
        } catch (Exception e) { e.printStackTrace(); } 
        finally { closeResource(conn, pstmt, rs); }
        return isDuplicate;
    }

    // 5. 내 예약 내역 조회
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

    // 6. 예약 취소 
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

    // 7. 실시간 예약 현황 확인
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