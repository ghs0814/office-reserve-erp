package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.office.dao.EmployeeDAO;

/**
 * 회원가입(사원 등록) 요청을 처리하는 컨트롤러입니다.
 * (사번을 기준으로 비밀번호만 업데이트하도록 수정됨)
 */
@WebServlet("/joinProcess.do")
public class JoinController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 한글 깨짐 방지를 위한 인코딩 설정
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<script>");

        try {
            // 1. JSP 가입 폼에서 입력받은 최소한의 데이터(사번, 비밀번호)만 수집합니다.
            // (이름과 직급은 이미 DB에 있으므로 수집할 필요가 없습니다)
            String empNo = request.getParameter("empNo");
            String empPw = request.getParameter("empPw");

            // 2. DAO를 호출하여 비밀번호를 업데이트합니다.
            EmployeeDAO dao = new EmployeeDAO();
            
            // 우리가 DAO 2단계에서 새로 만든 updateEmployeePassword 메서드를 사용합니다.
            boolean isSuccess = dao.updateEmployeePassword(empNo, empPw);

            // 3. 결과에 따른 사용자 알림 및 화면 이동을 처리합니다.
            if (isSuccess) {
                out.println("alert('비밀번호 설정 및 가입이 완료되었습니다. 로그인해 주세요.');");
                out.println("location.href='index.jsp';"); 
            } else {
                // 업데이트 실패 시 (예: 사번을 입력하지 않고 꼼수로 넘어왔을 때)
                out.println("alert('가입에 실패했습니다. 유효한 사번인지 다시 확인해 주세요.');");
                out.println("history.back();");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("alert('처리 중 오류가 발생했습니다.');");
            out.println("history.back();");
        }
        
        out.println("</script>");
        out.flush();
        out.close();
    }
}