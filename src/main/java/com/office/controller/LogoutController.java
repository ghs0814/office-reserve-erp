package com.office.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 로그아웃(세션 종료)을 처리하는 컨트롤러입니다.
 */
@WebServlet("/logout.do")
public class LogoutController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * 로그아웃 요청(GET 방식)을 처리합니다.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. 현재 접속 중인 사용자의 세션을 가져옵니다. (없으면 새로 만들지 않도록 false 설정)
        HttpSession session = request.getSession(false);
        
        // 2. 세션이 존재한다면 내부의 모든 데이터를 지우고 세션을 무효화합니다.
        if (session != null) {
            session.invalidate();
        }
        
        // 3. 로그아웃 완료 후 다시 로그인 화면(index.jsp)으로 강제 이동시킵니다.
        response.sendRedirect("index.jsp");
    }
}