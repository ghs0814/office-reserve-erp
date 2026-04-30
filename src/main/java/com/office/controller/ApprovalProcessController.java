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

@WebServlet("/approvalProcess.do")
public class ApprovalProcessController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String rentalNoStr = request.getParameter("rentalNo");
        String stepStr = request.getParameter("approvalStep");
        String action = request.getParameter("action"); 

        PrintWriter out = response.getWriter();
        out.println("<script>");

        if (rentalNoStr != null && action != null && stepStr != null) {
            int rentalNo = Integer.parseInt(rentalNoStr);
            int currentStep = Integer.parseInt(stepStr);
            String actionText = action.equals("approve") ? "승인" : "반려";
            
            // 세션에서 로그인한 관리자 이름 가져오기
            HttpSession session = request.getSession();
            EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
            String managerName = (loginEmp != null) ? loginEmp.getEmpName() : "관리자";
            
            RentalDAO rentalDao = new RentalDAO();
            boolean success = rentalDao.processStepApproval(rentalNo, currentStep, managerName, action);

            if(success) {
                out.println("alert('" + rentalNo + "번 기안이 성공적으로 " + actionText + " 처리되었습니다. (콘솔 확인)');");
                out.println("location.href='managerApproval.do';"); 
            } else {
                out.println("alert('처리 중 오류가 발생했습니다.');");
                out.println("history.back();");
            }
        } else {
            out.println("alert('잘못된 접근입니다.');");
            out.println("history.back();");
        }
        
        out.println("</script>");
        out.flush();
        out.close();
    }
}