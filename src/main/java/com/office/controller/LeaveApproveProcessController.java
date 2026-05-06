package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.office.dao.LeaveDAO;
import com.office.dto.EmployeeDTO;

/**
 * 휴가 상세 결재 화면(POST)에서 넘어온 승인/반려 요청을 처리하는 컨트롤러입니다.
 */
@WebServlet("/leaveApproveProcess.do")
public class LeaveApproveProcessController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");

        if (loginEmp == null) {
            out.println("<script>alert('로그인이 필요합니다.'); location.href='index.jsp';</script>");
            return;
        }

        // JSP에서 보낸 파라미터 수집
        int leaveNo = Integer.parseInt(request.getParameter("leaveNo"));
        int currentStep = Integer.parseInt(request.getParameter("currentStep"));
        String action = request.getParameter("action"); 
        boolean isApprove = "approve".equals(action);

        LeaveDAO dao = new LeaveDAO();
        // ★ LeaveDAO_3에 이미 구현된 processApproval 메서드를 호출합니다.
        boolean isSuccess = dao.processApproval(leaveNo, currentStep, loginEmp.getEmpName(), isApprove);

        out.println("<script>");
        if (isSuccess) {
            String msg = isApprove ? "승인 처리가 완료되었습니다." : "반려 처리가 완료되었습니다.";
            out.println("alert('" + msg + "');");
            out.println("location.href='documentList.do';"); 
        } else {
            out.println("alert('결재 처리 중 오류가 발생했습니다.');");
            out.println("history.back();");
        }
        out.println("</script>");
    }
}