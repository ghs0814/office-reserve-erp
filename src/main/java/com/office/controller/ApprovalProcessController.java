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
        String action = request.getParameter("action"); // 'approve' 또는 'reject'

        PrintWriter out = response.getWriter();
        out.println("<script>");

        if (rentalNoStr != null && action != null) {
            // DB 연동 전 임시 결과 메시지 세팅
            String actionText = action.equals("approve") ? "승인" : "반려";
            
            /*
            // [나중에 DB 연결 시 작성할 핵심 로직 미리보기]
            int rentalNo = Integer.parseInt(rentalNoStr);
            RentalDAO rentalDao = new RentalDAO();
            
            if (action.equals("approve")) {
                rentalDao.updateApprovalStatus(rentalNo, "대여중"); 
                // EquipmentDAO를 호출해서 비품 수량 -1 처리 로직 추가
            } else if (action.equals("reject")) {
                rentalDao.updateApprovalStatus(rentalNo, "반려됨");
            }
            */

            // 2. 결과 알림 및 페이지 이동
            out.println("alert('" + rentalNoStr + "번 기안이 성공적으로 " + actionText + " 처리되었습니다.');");
            out.println("location.href='managerApproval.do';"); // 다시 결재함 목록으로 이동
            
        } else {
            out.println("alert('잘못된 접근입니다.');");
            out.println("history.back();");
        }
        
        out.println("</script>");
        out.flush();
        out.close();
    }
}