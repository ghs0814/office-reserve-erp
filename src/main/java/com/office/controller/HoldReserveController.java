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

@WebServlet("/holdReserve.do")
public class HoldReserveController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        String roomId = request.getParameter("roomId");
        int empNo = Integer.parseInt(request.getParameter("empNo"));
        Date resDate = Date.valueOf(request.getParameter("resDate"));
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        // [백엔드 2차 방어선] 오늘 날짜이면서 현재 시간보다 이전 시간(과거)을 요청했는지 검증
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate reserveDate = resDate.toLocalDate();
        int startHour = Integer.parseInt(startTime.split(":")[0]);
        int currentHour = java.time.LocalTime.now().getHour();

        if (reserveDate.isEqual(today) && startHour <= currentHour) {
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
            // 화면 단(JS)에서 인식할 수 있도록 'past_time' 이라는 에러 코드를 반환합니다.
            out.print("{\"result\":\"past_time\"}");
            out.flush();
            out.close();
            return; // 검증에 실패했으므로 여기서 컨트롤러 실행을 즉시 종료합니다.
        }

        ReservationDTO dto = new ReservationDTO();
        dto.setRoomId(roomId);
        dto.setEmpNo(empNo);
        dto.setResDate(resDate);
        dto.setStartTime(startTime);
        dto.setEndTime(endTime);

        ReservationDAO dao = new ReservationDAO();
        // DAO에서 겹침 방지(Overlap) 로직을 통과하면 새 예약 번호를 줍니다.
        int newResNo = dao.holdReservation(dto);

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        if (newResNo > 0) {
            out.print("{\"result\":\"success\", \"resNo\":" + newResNo + "}");
        } else {
            out.print("{\"result\":\"fail\"}");
        }
        
        out.flush();
        out.close();
    }
}