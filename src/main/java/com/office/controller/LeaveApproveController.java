package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.office.dao.LeaveDAO;
import com.office.dto.EmployeeDTO;

@WebServlet("/leaveApproveAction.do") // 이름 변경하여 구분
public class LeaveApproveController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        int leaveNo = Integer.parseInt(request.getParameter("leaveNo"));
        int step = Integer.parseInt(request.getParameter("approvalStep"));
        String action = request.getParameter("action");

        LeaveDAO dao = new LeaveDAO();
        boolean success = dao.processStepApproval(leaveNo, step, loginEmp.getEmpName(), action);

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>");
        if (success) {
            out.println("alert('휴가 결재가 처리되었습니다.');");
            out.println("location.href='managerApproval.do';");
        } else {
            out.println("alert('처리 실패'); history.back();");
        }
        out.println("</script>");
    }
}