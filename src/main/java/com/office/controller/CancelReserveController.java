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

        String resNoStr = request.getParameter("resNo");
        // 마이페이지에서 넘어왔는지 확인하는 꼬리표
        String from = request.getParameter("from"); 
        
        if (resNoStr == null || resNoStr.trim().isEmpty()) {
            response.sendRedirect("myPage.do");
            return;
        }
        
        int resNo = Integer.parseInt(resNoStr);

        ReservationDAO dao = new ReservationDAO();
        boolean isSuccess = dao.cancelReservation(resNo);

        PrintWriter out = response.getWriter();
        out.println("<script>");
        if (isSuccess) {
            out.println("alert('예약이 성공적으로 취소되었습니다.');");
        } else {
            out.println("alert('예약 취소 처리에 실패했습니다.');");
        }
        
        // 꼬리표가 'mypage'면 마이페이지로, 아니면 기존 내 예약 목록으로 돌아감
        if ("mypage".equals(from)) {
            out.println("location.href='myPage.do';"); 
        } else {
            out.println("location.href='myReserveList.do';"); 
        }
        
        out.println("</script>");
        out.flush();
        out.close();
    }
}