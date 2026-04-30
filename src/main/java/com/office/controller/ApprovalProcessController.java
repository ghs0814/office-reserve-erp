package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/approvalProcess.do")
public class ApprovalProcessController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 1. 화면에서 넘어온 파라미터 받기
        String rentalNoStr = request.getParameter("rentalNo");
        String stepStr = request.getParameter("approvalStep");
        String action = request.getParameter("action"); // 'approve' 또는 'reject'

        PrintWriter out = response.getWriter();
        out.println("<script>");

        if (rentalNoStr != null && action != null && stepStr != null) {
            int rentalNo = Integer.parseInt(rentalNoStr);
            int currentStep = Integer.parseInt(stepStr);
            String actionText = action.equals("approve") ? "승인" : "반려";
            
            // [콘솔 테스트 출력] DB에 날아갈 로직 시뮬레이션
            System.out.println("=== 결재 처리 시뮬레이션 ===");
            System.out.println("대상 번호: " + rentalNo);
            System.out.println("요청 단계: " + currentStep);
            if (action.equals("approve")) {
                if(currentStep < 5) {
                    System.out.println("결과: SIGN" + currentStep + " 업데이트 및 " + (currentStep + 1) + "단계로 이동");
                } else {
                    System.out.println("결과: 최종 5단계 승인 완료 (STATUS='승인완료', 비품 재고 -1)");
                }
            } else {
                System.out.println("결과: 기안 반려 (모든 SIGN NULL 처리, 1단계 리셋, STATUS='미승인')");
            }
            
            /*
            // [나중에 DB 연결 시 주석을 풀고 사용할 DAO 로직]
            RentalDAO rentalDao = new RentalDAO();
            String managerName = "현재로그인한관리자명";
            
            boolean success = rentalDao.processStepApproval(rentalNo, currentStep, managerName, action);
            if(success && action.equals("approve") && currentStep == 5) {
                // EquipmentDAO를 호출해서 비품 수량 -1 처리 로직 실행
            }
            */

            // 2. 결과 알림 및 페이지 이동
            out.println("alert('" + rentalNo + "번 기안이 성공적으로 " + actionText + " 처리되었습니다. (콘솔 로그 확인)');");
            out.println("location.href='managerApproval.do';"); 
            
        } else {
            out.println("alert('잘못된 접근입니다.');");
            out.println("history.back();");
        }
        
        out.println("</script>");
        out.flush();
        out.close();
    }
}