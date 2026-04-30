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
    	// 진단용 로그 추가
        System.out.println("--- LoginController 진입 성공! ---");
        System.out.println("입력받은 ID: " + request.getParameter("loginId"));
        // 1. 한글 깨짐 방지 설정 (POST 방식에서는 필수)
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 2. 화면(index.jsp)에서 입력한 데이터 꺼내기
        // 큰따옴표 안의 글자는 반드시 index.jsp의 input 태그 name 속성과 똑같아야 합니다.
        String loginId = request.getParameter("loginId");
        String loginPw = request.getParameter("loginPw");

        // 3. DAO를 통해 DB에 일치하는 사원이 있는지 확인
        EmployeeDAO dao = new EmployeeDAO();
        //EmployeeDTO dto = dao.loginCheck(loginId, loginPw);
        //로그인 체크 임시 주석처리
     // 2. 가짜 데이터를 강제로 생성
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmpNo(202604);
        dto.setLoginId(loginId);
        dto.setEmpName("테스트사원");
        dto.setLoginPw(loginPw);
        
        // 4. 확인 결과에 따른 처리
        //dto != null
        if (true) {
            // [로그인 성공] 
            // HttpSession: 브라우저가 꺼지기 전까지 유지되는 사용자만의 개인 보관함
            HttpSession session = request.getSession();
            
            // DB에서 가져온 사원 정보(dto)를 'loginEmp'라는 이름으로 보관함에 넣습니다.
            session.setAttribute("loginEmp", dto);
            
            // 로그인이 완료되었으니 메인 화면(main.jsp)으로 강제 이동시킵니다.
            response.sendRedirect("main.jsp"); 
            
        } else {
            // [로그인 실패]
            // 브라우저 화면에 경고창(alert)을 띄우는 자바스크립트 코드를 직접 쏴줍니다.
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('사번 또는 비밀번호가 일치하지 않습니다.');");
            out.println("history.back();"); // 사용자를 다시 이전 로그인 화면으로 돌려보냅니다.
            out.println("</script>");
        }
    }
}