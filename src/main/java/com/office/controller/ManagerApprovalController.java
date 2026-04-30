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
import com.office.dto.EmployeeDTO;
import com.office.dto.RentalHistoryDTO;

@WebServlet("/managerApproval.do")
public class ManagerApprovalController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        if (loginEmp == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // 현재 로그인한 관리자의 등급 추출
        int managerLevel = loginEmp.getEmpLevel();

        RentalDAO dao = new RentalDAO();
        // 자신의 등급에 해당하는 문서만 가져오기
        List<RentalHistoryDTO> approvalList = dao.getPendingList(managerLevel);

        request.setAttribute("approvalList", approvalList);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("managerApproval.jsp");
        dispatcher.forward(request, response);
    }
}