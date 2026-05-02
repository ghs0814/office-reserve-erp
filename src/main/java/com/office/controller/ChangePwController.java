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

@WebServlet("/changePwProcess.do")
public class ChangePwController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 1. 로그인 세션 확인
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");

        if (loginEmp == null) {
            out.println("<script>alert('로그인이 필요합니다.'); location.href='index.jsp';</script>");
            return;
        }

        // 2. JSP 폼에서 넘어온 파라미터 받기
        String currentPw = request.getParameter("currentPw");
        String newPw = request.getParameter("newPw");

        out.println("<script>");
        
        // 3. 현재 비밀번호가 맞는지 검증 (DB에 있는 비밀번호와 입력한 현재 비밀번호 비교)
        if (!loginEmp.getEmpPw().equals(currentPw)) {
            out.println("alert('현재 비밀번호가 일치하지 않습니다.');");
            out.println("history.back();");
        } else {
            // 4. 비밀번호가 맞다면 DAO를 호출하여 새 비밀번호로 업데이트
            EmployeeDAO dao = new EmployeeDAO();
            boolean isSuccess = dao.updatePassword(loginEmp.getEmpNo(), newPw);

            if (isSuccess) {
                // 5. 성공 시 세션을 날리고(로그아웃) 다시 로그인하도록 유도
                session.invalidate();
                out.println("alert('비밀번호가 성공적으로 변경되었습니다. 다시 로그인해주세요.');");
                out.println("location.href='index.jsp';"); 
            } else {
                out.println("alert('비밀번호 변경 중 오류가 발생했습니다.');");
                out.println("history.back();");
            }
        }
        
        out.println("</script>");
        out.flush();
        out.close();
    }
}