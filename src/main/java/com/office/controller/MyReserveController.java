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

@WebServlet("/myReserveList.do")
public class MyReserveController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	

    	HttpSession session = request.getSession();
    	EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    	if (loginEmp == null) {
    	    response.sendRedirect("index.jsp");
    	    return;
    	}

    	ReservationDAO dao = new ReservationDAO();
    	// 현재 로그인한 사원의 번호로 예약 내역 조회
    	List<ReservationDTO> reserveList = dao.getMyReservations(loginEmp.getEmpNo());

    	request.setAttribute("reserveList", reserveList);

    	RequestDispatcher dispatcher = request.getRequestDispatcher("myReserveList.jsp");
    	dispatcher.forward(request, response);
    }
}