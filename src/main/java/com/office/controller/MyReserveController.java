package com.office.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.office.dto.EmployeeDTO;
import com.office.dto.ReservationDTO;

@WebServlet("/myReserveList.do")
public class MyReserveController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. 세션 검사
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        if (loginEmp == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // 2. DB 대신 보여줄 가짜(Dummy) 데이터 리스트 생성
        List<ReservationDTO> reserveList = new ArrayList<>();
        
        // 가짜 데이터 1
        ReservationDTO dto1 = new ReservationDTO(
            1, loginEmp.getEmpNo(), "403", Date.valueOf("2026-05-10"), 
            "10:00", "12:00", "프로젝트 주간 회의", "예약완료"
        );
        
        // 가짜 데이터 2
        ReservationDTO dto2 = new ReservationDTO(
            2, loginEmp.getEmpNo(), "Interview", Date.valueOf("2026-05-12"), 
            "14:00", "16:00", "신입사원 1차 면접", "예약완료"
        );

        reserveList.add(dto1);
        reserveList.add(dto2);

        // 3. 리스트를 request에 담아서 JSP로 포워딩
        request.setAttribute("reserveList", reserveList);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("myReserveList.jsp");
        dispatcher.forward(request, response);
    }
}