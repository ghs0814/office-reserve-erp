package com.office.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.office.dto.EmployeeDTO;

@WebServlet("/leaveForm.do")
public class LeaveFormController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");

        if (loginEmp == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        // 화면으로 이동
        request.getRequestDispatcher("leaveForm.jsp").forward(request, response);
    }
}