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
 * 관리자 결재 승인/반려 프로세스 처리 컨트롤러
 */
@WebServlet("/approvalProcess.do")
public class ApprovalProcessController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 인코딩 및 응답 형식 설정
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 1. 결재 처리에 필요한 파라미터(대여번호, 현재단계, 승인/반려 여부) 수집
        String rentalNoStr = request.getParameter("rentalNo");
        String stepStr = request.getParameter("approvalStep");
        String action = request.getParameter("action"); 

        PrintWriter out = response.getWriter();
        out.println("<script>");

        // 2. 파라미터 유효성 검사
        if (rentalNoStr != null && action != null && stepStr != null) {
            int rentalNo = Integer.parseInt(rentalNoStr);
            int currentStep = Integer.parseInt(stepStr);
            String actionText = action.equals("approve") ? "승인" : "반려";
            
            // 3. 세션에서 현재 로그인한 관리자의 정보를 가져와 서명인 이름으로 사용
            HttpSession session = request.getSession();
            EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
            String managerName = (loginEmp != null) ? loginEmp.getEmpName() : "관리자";
            
            // 4. RentalDAO를 통해 다단계 결재 처리 수행
            RentalDAO rentalDao = new RentalDAO();
            boolean success = rentalDao.processStepApproval(rentalNo, currentStep, managerName, action);

            // 5. 처리 결과에 따른 알림 및 페이지 이동
            if(success) {
                out.println("alert('" + rentalNo + "번 기안이 성공적으로 " + actionText + " 처리되었습니다.');");
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