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

import com.office.dao.LeaveDAO;
import com.office.dao.RentalDAO;
import com.office.dto.EmployeeDTO;
import com.office.dto.LeaveHistoryDTO;
import com.office.dto.RentalHistoryDTO;

/**
 * 관리자별 권한 등급에 맞는 결재 대기 목록을 조회하는 컨트롤러입니다.
 */
@WebServlet("/managerApproval.do")
public class ManagerApprovalController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (loginEmp == null || !"Y".equals(loginEmp.getManager())) {
            response.sendRedirect("index.jsp");
            return;
        }

        int managerLevel = loginEmp.getEmpLevel();

        // 1. 비품 대여 결재 대기 목록
        RentalDAO rentalDao = new RentalDAO();
        List<RentalHistoryDTO> approvalList = rentalDao.getPendingList(managerLevel);
        request.setAttribute("approvalList", approvalList);

        // 2. 휴가 신청 결재 대기 목록 추가
        LeaveDAO leaveDao = new LeaveDAO();
        List<LeaveHistoryDTO> leaveList = leaveDao.getPendingLeaveList(managerLevel); 
        request.setAttribute("leaveList", leaveList);
        
        request.getRequestDispatcher("managerApproval.jsp").forward(request, response);
    }
}