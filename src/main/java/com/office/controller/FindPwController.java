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

        if (empNoStr != null && empName != null) {
            try {
                int empNo = Integer.parseInt(empNoStr);
                
                // 2. DAO를 통해 회원 정보 존재 여부 확인
                EmployeeDAO dao = new EmployeeDAO();
                String foundPw = dao.findPassword(empNo, empName);

                if (foundPw != null) {
                    // 일치하는 정보가 있으면 사번을 request에 담아 새 비밀번호 폼으로 이동
                    request.setAttribute("empNo", empNoStr);
                    request.getRequestDispatcher("resetPw.jsp").forward(request, response);
                } else {
                    out.println("<script>");
                    out.println("alert('입력하신 사번과 이름에 일치하는 정보가 없습니다.');");
                    out.println("history.back();");
                    out.println("</script>");
                }
            } catch (NumberFormatException e) {
                out.println("<script>");
                out.println("alert('사원 번호는 숫자만 입력 가능합니다.');");
                out.println("history.back();");
                out.println("</script>");
            }
        } else {
            out.println("<script>");
            out.println("alert('잘못된 접근입니다.');");
            out.println("history.back();");
            out.println("</script>");
        }
    }
}