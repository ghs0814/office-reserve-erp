package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.office.dao.EmployeeDAO;
import com.office.dto.EmployeeDTO;

// 웹 브라우저에서 /login.do 라는 주소를 호출하면 이 클래스가 실행됩니다.
@WebServlet("/login.do")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    

    // index.jsp의 form 태그에서 method="post"로 보냈기 때문에 doPost 메서드가 실행됩니다.
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html; charset=UTF-8");

    	String loginId = request.getParameter("loginId");
    	String loginPw = request.getParameter("loginPw");

    	// 1. DAO를 통해 실제 DB 데이터 조회
    	EmployeeDAO dao = new EmployeeDAO();
    	EmployeeDTO dto = dao.loginCheck(loginId, loginPw);

    	// 2. 조회 결과에 따른 처리
    	if (dto != null) {
    	    HttpSession session = request.getSession();
    	    session.setAttribute("loginEmp", dto);
    	    response.sendRedirect("main.jsp"); 
    	} else {
    	    PrintWriter out = response.getWriter();
    	    out.println("<script>");
    	    out.println("alert('사번 또는 비밀번호가 일치하지 않습니다.');");
    	    out.println("history.back();");
    	    out.println("</script>");
    	}
    }
}