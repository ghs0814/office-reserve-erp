package com.office.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout.do")
public class LogoutController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. 현재 접속 중인 세션 가져오기 (세션이 없으면 굳이 새로 만들지 않음)
        HttpSession session = request.getSession(false);
        
        // 2. 세션이 존재한다면 완전히 삭제(무효화) 처리
        if (session != null) {
            session.invalidate();
        }
        
        // 3. 로그인 화면으로 강제 이동
        response.sendRedirect("index.jsp");
    }
}