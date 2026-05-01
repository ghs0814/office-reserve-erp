package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.office.dao.ReservationDAO;

/**
 * 회의실 예약 취소 요청을 처리하는 서블릿
 * URL: /cancelReserve.do
 */
@WebServlet("/cancelReserve.do")
public class CancelReserveController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 한글 깨짐 방지 및 응답 형식 설정
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 1. URL 파라미터로 넘어온 예약 번호(resNo) 수집
        String resNoStr = request.getParameter("resNo");
        
        // 2. 비정상적인 접근(파라미터 누락) 방지 로직
        if (resNoStr == null || resNoStr.trim().isEmpty()) {
            response.sendRedirect("myReserveList.do");
            return;
        }
        
        int resNo = Integer.parseInt(resNoStr);

        // 3. DAO를 호출하여 DB 상의 예약 상태를 '취소됨'으로 업데이트
        ReservationDAO dao = new ReservationDAO();
        boolean isSuccess = dao.cancelReservation(resNo);

        // 4. 처리 결과 알림 후 내 예약 목록 페이지로 리다이렉트
        PrintWriter out = response.getWriter();
        out.println("<script>");
        if (isSuccess) {
            out.println("alert('예약이 성공적으로 취소되었습니다.');");
        } else {
            out.println("alert('예약 취소 처리에 실패했습니다.');");
        }
        
        // 5. 예약 취소 후 화면 갱신을 위해 목록 컨트롤러를 다시 호출하여 이동
        out.println("location.href='myReserveList.do';"); 
        out.println("</script>");
        out.flush();
        out.close();
    }
}