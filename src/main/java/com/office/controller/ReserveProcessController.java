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
 * 5분 타이머 내에 확인 버튼을 눌렀을 때, 임시 예약을 '예약완료'로 확정하는 컨트롤러입니다.
 */
@WebServlet("/reserveProcess.do")
public class ReserveProcessController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 이제 방금 임시선점된 예약 번호(resNo)와 목적(purpose)만 있으면 됩니다.
        String resNoStr = request.getParameter("resNo");
        String purpose = request.getParameter("purpose");
        String roomId = request.getParameter("roomId"); // 실패 시 돌아갈 방 번호

        PrintWriter out = response.getWriter();
        out.println("<script>");

        if (resNoStr == null || resNoStr.isEmpty()) {
            out.println("alert('시간을 먼저 선택해 주세요.');");
            out.println("history.back();");
        } else {
            int resNo = Integer.parseInt(resNoStr);
            ReservationDAO dao = new ReservationDAO();
            
            // DAO를 호출하여 확정 처리 (5분이 지났으면 실패 처리됨)
            boolean isSuccess = dao.confirmReservation(resNo, purpose);

            if (isSuccess) {
                out.println("alert('회의실 예약이 성공적으로 확정되었습니다.');");
                // 마이페이지로 이동
                out.println("location.href='myPage.do';"); 
            } else {
                out.println("alert('예약 확정 시간이 초과되었거나 이미 취소된 예약입니다. 다시 선택해 주세요.');");
                // 다시 해당 회의실 예약 첫 화면으로 튕겨냅니다.
                out.println("location.href='reserve.do?roomId=" + roomId + "';");
            }
        }
        
        out.println("</script>");
        out.flush();
        out.close();
    }
}