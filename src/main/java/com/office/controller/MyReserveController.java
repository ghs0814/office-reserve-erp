package com.office.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.office.dao.ReservationDAO;
import com.office.dto.EmployeeDTO;
import com.office.dto.ReservationDTO;

/**
 * 로그인한 사원의 회의실 예약 내역을 조회하는 컨트롤러입니다.
 */
@WebServlet("/myReserveList.do")
public class MyReserveController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	// 1. 세션에서 로그인 객체를 확인합니다.[cite: 59]
    	HttpSession session = request.getSession();
    	EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    	
        if (loginEmp == null) {
    	    response.sendRedirect("index.jsp");
    	    return;
    	}

    	// 2. ReservationDAO를 호출하여 현재 로그인한 사원의 번호로 예약 내역을 조회합니다.[cite: 59]
    	ReservationDAO dao = new ReservationDAO();
    	List<ReservationDTO> reserveList = dao.getMyReservations(loginEmp.getEmpNo());

    	// 3. 조회 결과를 request 영역에 담습니다.[cite: 59]
    	request.setAttribute("reserveList", reserveList);

    	// 4. 내 예약 목록 화면(myReserveList.jsp)으로 포워딩합니다.[cite: 59]
    	RequestDispatcher dispatcher = request.getRequestDispatcher("myReserveList.jsp");
    	dispatcher.forward(request, response);
    }
}