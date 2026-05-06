package com.office.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.office.dao.LeaveDAO;
import com.office.dto.LeaveHistoryDTO;

@WebServlet(urlPatterns = {"/leaveDetail.do", "/leaveStatus.do"})
public class LeaveDetailController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int leaveNo = Integer.parseInt(request.getParameter("leaveNo"));
        
        LeaveDAO dao = new LeaveDAO();
        LeaveHistoryDTO doc = dao.getLeaveDetail(leaveNo);
        
        request.setAttribute("doc", doc);

        String uri = request.getRequestURI();
        if (uri.contains("leaveDetail.do")) {
            request.getRequestDispatcher("leaveDetail.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("leaveStatus.jsp").forward(request, response);
        }
    }
}