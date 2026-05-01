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

/**
 * 사용자 로그인을 처리하는 컨트롤러입니다.
 */
@WebServlet("/login.do")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * 로그인 폼(POST 방식)으로부터 데이터를 전달받아 처리합니다.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// 인코딩 및 응답 형식 설정
        request.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html; charset=UTF-8");

        // 1. 사용자가 입력한 아이디와 비밀번호 파라미터를 가져옵니다.
    	String loginId = request.getParameter("loginId");
    	String loginPw = request.getParameter("loginPw");

    	// 2. DAO를 통해 데이터베이스에 실제 일치하는 사원 정보가 있는지 확인합니다.
    	EmployeeDAO dao = new EmployeeDAO();
    	EmployeeDTO dto = dao.loginCheck(loginId, loginPw);

    	// 3. 인증 결과에 따른 후속 처리를 수행합니다.
    	if (dto != null) {
            // 로그인 성공: 세션을 생성하고 사원 정보를 저장한 뒤 메인 페이지로 이동합니다.
    	    HttpSession session = request.getSession();
    	    session.setAttribute("loginEmp", dto);
    	    response.sendRedirect("main.jsp"); 
    	} else {
            // 로그인 실패: 안내 메시지를 띄우고 이전 페이지(로그인 폼)로 되돌아갑니다.
    	    PrintWriter out = response.getWriter();
    	    out.println("<script>");
    	    out.println("alert('사번 또는 비밀번호가 일치하지 않습니다.');");
    	    out.println("history.back();");
    	    out.println("</script>");
    	}
    }
}