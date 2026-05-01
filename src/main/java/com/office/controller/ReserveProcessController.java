package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.office.dao.ReservationDAO;
import com.office.dto.ReservationDTO;

/**
 * 회의실 예약 정보를 검증하고 DB에 등록하는 컨트롤러입니다.
 */
@WebServlet("/reserveProcess.do")
public class ReserveProcessController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 1. 예약 폼에서 전송한 데이터(방 ID, 사번, 날짜, 시간, 목적 등)를 수집합니다.
        String roomId = request.getParameter("roomId");
        int empNo = Integer.parseInt(request.getParameter("empNo"));
        
        // 날짜 형식 변환 시 발생할 수 있는 예외를 방어합니다.
        Date resDate = null;
        try {
            resDate = Date.valueOf(request.getParameter("resDate"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String purpose = request.getParameter("purpose");

        ReservationDAO dao = new ReservationDAO();
        PrintWriter out = response.getWriter();
        out.println("<script>");

        // 2. [중복 검사] 선택한 회의실/날짜/시간에 이미 예약된 내역이 있는지 확인합니다.
        boolean isDuplicate = dao.checkDuplicate(roomId, resDate, startTime);

        if (isDuplicate) {
            // 이미 예약된 시간이면 안내 후 이전 화면으로 돌려보냅니다.
            out.println("alert('이미 해당 시간에 예약이 존재합니다. 다른 시간을 선택해주세요.');");
            out.println("history.back();");
        } else {
            // 3. 중복이 없다면 DTO 객체에 예약 데이터를 담아 저장 준비를 합니다.
            ReservationDTO dto = new ReservationDTO();
            dto.setRoomId(roomId);
            dto.setEmpNo(empNo);
            dto.setResDate(resDate);
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            dto.setPurpose(purpose);

            // 4. ReservationDAO를 호출하여 DB에 최종 예약 내역을 추가(Insert)합니다.
            boolean isSuccess = dao.insertReservation(dto);

            if (isSuccess) {
                out.println("alert('회의실 예약이 성공적으로 완료되었습니다.');");
                // 예약 완료 후 나의 예약 목록 페이지로 이동합니다.
                out.println("location.href='myReserveList.do';"); 
            } else {
                out.println("alert('예약 처리 중 오류가 발생했습니다.');");
                out.println("history.back();");
            }
        }
        
        out.println("</script>");
        out.flush();
        out.close();
    }
}