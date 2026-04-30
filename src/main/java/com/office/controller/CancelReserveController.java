package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.office.dao.ReservationDAO;

@WebServlet("/cancelReserve.do")
public class CancelReserveController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 1. 취소할 예약 번호(resNo) 파라미터 받기
        String resNoStr = request.getParameter("resNo");
        
        // 비정상적인 접근 방지
        if (resNoStr == null || resNoStr.trim().isEmpty()) {
            response.sendRedirect("myReserveList.do");
            return;
        }
        
        int resNo = Integer.parseInt(resNoStr);

        // 2. DAO 호출하여 예약 상태를 '취소됨'으로 변경
        ReservationDAO dao = new ReservationDAO();
        boolean isSuccess = dao.cancelReservation(resNo);

        // 3. 결과에 따른 알림창 띄우고 다시 목록 화면으로 이동
        PrintWriter out = response.getWriter();
        out.println("<script>");
        if (isSuccess) {
            out.println("alert('예약이 성공적으로 취소되었습니다.');");
        } else {
            out.println("alert('예약 취소 처리에 실패했습니다.');");
        }
        // 처리 완료 후 다시 내 예약 리스트 컨트롤러를 호출하여 화면 갱신
        out.println("location.href='myReserveList.do';"); 
        out.println("</script>");
        out.flush();
        out.close();
    }
}