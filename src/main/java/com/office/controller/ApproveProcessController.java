package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.office.dao.RentalDAO;
import com.office.dto.EmployeeDTO;

/**
 * 3분할 화면에서 넘어온 '승인' 또는 '반려' 요청을 처리하는 최종 결재 컨트롤러입니다.
 */
@WebServlet("/approveProcess.do")
public class ApproveProcessController extends HttpServlet {
    private static final long serialVersionUID = 1L;

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

        // JSP에서 넘어온 파라미터 받기
        int rentalNo = Integer.parseInt(request.getParameter("rentalNo"));
        int eqNo = Integer.parseInt(request.getParameter("eqNo")); 
        int currentStep = Integer.parseInt(request.getParameter("currentStep"));
        String action = request.getParameter("action"); 
        
        // 승인 버튼을 눌렀는지 여부
        boolean isApprove = "approve".equals(action);

        RentalDAO dao = new RentalDAO();
        // DAO의 통합 결재 로직 호출
        boolean isSuccess = dao.processApproval(rentalNo, eqNo, currentStep, loginEmp.getEmpName(), isApprove);

        out.println("<script>");
        if (isSuccess) {
            String msg = isApprove ? "승인 처리가 완료되었습니다." : "반려 처리가 완료되었습니다.";
            out.println("alert('" + msg + "');");
            out.println("location.href='documentList.do';"); // 처리 후 목록으로 이동
        } else {
            out.println("alert('결재 처리 중 오류가 발생했습니다. (원인: 비품 재고 부족 등)');");
            out.println("history.back();");
        }
        out.println("</script>");
        out.flush();
        out.close();
    }
}