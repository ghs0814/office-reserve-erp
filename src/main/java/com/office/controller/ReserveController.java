package com.office.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// main.jsp에서 reserve.do?roomId=... 로 호출하면 이 서블릿이 실행됩니다.
@WebServlet("/reserve.do")
public class ReserveController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // location.href를 통한 이동은 기본적으로 GET 방식이므로 doGet을 사용합니다.
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. 한글 깨짐 방지
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 2. 세션 확인 (로그인하지 않은 사용자의 접근 차단)
        HttpSession session = request.getSession();
        if (session.getAttribute("loginEmp") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // 3. main.jsp에서 넘겨준 방 번호(roomId) 파라미터 받기
        String roomId = request.getParameter("roomId");

        // 4. 받은 방 번호를 request 객체에 담아서 reserve.jsp로 전달할 준비
        request.setAttribute("selectedRoomId", roomId);

        // 5. 화면 이동 (Forward 방식 사용)
        // sendRedirect를 쓰면 파라미터가 날아가기 때문에, 데이터를 유지하는 forward를 사용합니다.
        RequestDispatcher dispatcher = request.getRequestDispatcher("reserve.jsp");
        dispatcher.forward(request, response);
    }

    // 만약 POST로 데이터가 들어와도 doGet에서 똑같이 처리하도록 연결해 줍니다.
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}