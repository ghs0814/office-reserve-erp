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

import com.office.dao.RentalDAO;
import com.office.dao.ReservationDAO;
import com.office.dto.EmployeeDTO;
import com.office.dto.RentalHistoryDTO;
import com.office.dto.ReservationDTO;

@WebServlet("/myPage.do")
public class MyPageController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (loginEmp == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // 1. 내 비품 대여 내역
        RentalDAO rentalDao = new RentalDAO();
        List<RentalHistoryDTO> myList = rentalDao.getMyRentalList(loginEmp.getEmpNo()); 
        request.setAttribute("myList", myList);

        // 2. 내 회의실 예약 내역
        ReservationDAO reserveDao = new ReservationDAO();
        List<ReservationDTO> reserveList = reserveDao.getMyReservations(loginEmp.getEmpNo());
        request.setAttribute("reserveList", reserveList);

        // ★ 3. 내 휴가 신청 내역 추가 (이 부분이 없으면 JSP에서 에러남)
        com.office.dao.LeaveDAO leaveDao = new com.office.dao.LeaveDAO();
        List<com.office.dto.LeaveHistoryDTO> leaveList = leaveDao.getMyLeaveList(loginEmp.getEmpNo());
        request.setAttribute("leaveList", leaveList);

        request.getRequestDispatcher("myPage.jsp").forward(request, response);
    }
}