package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.office.dao.EmployeeDAO;

@WebServlet("/findPwProcess.do")
public class FindPwController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 1. JSP에서 넘겨준 사번과 이름 받기
        String empNoStr = request.getParameter("empNo");
        String empName = request.getParameter("empName");

        out.println("<script>");
        
        if (empNoStr != null && empName != null) {
            try {
                int empNo = Integer.parseInt(empNoStr);
                
                // 2. DAO를 통해 비밀번호 조회
                EmployeeDAO dao = new EmployeeDAO();
                String foundPw = dao.findPassword(empNo, empName);

                // 3. 결과에 따른 처리
                if (foundPw != null) {
                    // 일치하는 정보가 있으면 비밀번호를 알려주고 로그인 화면으로 보냅니다.
                    out.println("alert('회원님의 비밀번호는 [" + foundPw + "] 입니다. 로그인 후 가급적 비밀번호를 변경해 주세요.');");
                    out.println("location.href='index.jsp';");
                } else {
                    // 일치하는 정보가 없으면 뒤로가기
                    out.println("alert('입력하신 사번과 이름에 일치하는 정보가 없습니다.');");
                    out.println("history.back();");
                }
            } catch (NumberFormatException e) {
                out.println("alert('사원 번호는 숫자만 입력 가능합니다.');");
                out.println("history.back();");
            }
        } else {
            out.println("alert('잘못된 접근입니다.');");
            out.println("history.back();");
        }
        
        out.println("</script>");
        out.flush();
        out.close();
    }
}