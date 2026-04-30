package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/reserveProcess.do")
public class ReserveProcessController extends HttpServlet {
    private static final long serialVersionUID = 1L;

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. 한글 깨짐 방지
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 2. ReservationDTO 구조에 맞춰 파라미터 받기
        String roomId = request.getParameter("roomId");
        String empNoStr = request.getParameter("empNo");
        String resDate = request.getParameter("resDate");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String purpose = request.getParameter("purpose");
        String status = request.getParameter("status");

        // 3. 콘솔 출력 테스트
        System.out.println("=== 예약 신청 데이터 확인 ===");
        System.out.println("방 번호: " + roomId);
        System.out.println("사원 번호: " + empNoStr);
        System.out.println("날짜: " + resDate);
        System.out.println("시간: " + startTime + " ~ " + endTime);
        System.out.println("목적: " + purpose);
        System.out.println("상태: " + status);
        System.out.println("=============================");

        // 4. 화면 이동 처리
        PrintWriter out = response.getWriter();
        out.println("<script>");
        out.println("alert('예약이 완료되었습니다! (임시 테스트 성공)');");
        out.println("location.href='main.jsp';"); 
        out.println("</script>");
        out.flush();
        out.close();
    }
}