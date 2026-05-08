package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.office.dao.EmployeeDAO;

@WebServlet("/resetPwProcess.do")
public class ResetPwController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        String empNo = request.getParameter("empNo");
        String newPw = request.getParameter("newPw");

        out.println("<script>");
        if (empNo != null && newPw != null) {
            EmployeeDAO dao = new EmployeeDAO();
            
            // 기존에 회원가입(JoinController)에서 쓰던 비밀번호 변경 메서드를 그대로 재활용합니다.
            boolean isSuccess = dao.updateEmployeePassword(empNo, newPw);

            if (isSuccess) {
                out.println("alert('비밀번호가 성공적으로 변경되었습니다. 새로운 비밀번호로 로그인해 주세요.');");
                out.println("location.href='index.jsp';");
            } else {
                out.println("alert('비밀번호 변경 중 시스템 오류가 발생했습니다. 다시 시도해 주세요.');");
                out.println("history.back();");
            }
        } else {
            out.println("alert('잘못된 접근입니다.');");
            out.println("location.href='index.jsp';");
        }
        out.println("</script>");
    }
}