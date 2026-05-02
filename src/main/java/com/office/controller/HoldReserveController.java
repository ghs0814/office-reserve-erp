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
 * 시간 버튼 클릭 시 AJAX 요청을 받아 5분 '임시선점'을 처리하는 컨트롤러입니다.
 */
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

        ReservationDTO dto = new ReservationDTO();
        dto.setRoomId(roomId);
        dto.setEmpNo(empNo);
        dto.setResDate(resDate);
        dto.setStartTime(startTime);
        dto.setEndTime(endTime);

        ReservationDAO dao = new ReservationDAO();
        // DAO를 호출하여 임시선점을 걸고 새로운 예약 번호를 받아옵니다. (-1이면 이미 선점당한 것)
        int newResNo = dao.holdReservation(dto);

        // JSON 형태로 브라우저에 응답을 보냅니다.
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