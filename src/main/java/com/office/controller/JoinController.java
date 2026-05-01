package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.office.dao.EmployeeDAO;
import com.office.dto.EmployeeDTO;

/**
 * 회원가입(사원 등록) 요청을 처리하는 컨트롤러입니다.
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
            // 1. JSP 가입 폼에서 입력받은 사원 데이터를 수집합니다.
            int empNo = Integer.parseInt(request.getParameter("empNo"));
            String loginId = request.getParameter("loginId");
            String loginPw = request.getParameter("loginPw");
            String empName = request.getParameter("empName");
            int empLevel = Integer.parseInt(request.getParameter("empLevel")); 

            // 2. 수집한 데이터를 DTO(Data Transfer Object)에 담아 전달할 준비를 합니다.
            EmployeeDTO dto = new EmployeeDTO();
            dto.setEmpNo(empNo);
            dto.setLoginId(loginId);
            dto.setLoginPw(loginPw);
            dto.setEmpName(empName);
            dto.setEmpLevel(empLevel);

            // 3. DAO를 호출하여 실제 데이터베이스에 사원 정보를 삽입(Insert)합니다.
            EmployeeDAO dao = new EmployeeDAO();
            boolean isSuccess = dao.insertEmployee(dto);

            // 4. 결과에 따른 사용자 알림 및 화면 이동을 처리합니다.
            if (isSuccess) {
                out.println("alert('회원가입이 완료되었습니다. 로그인해 주세요.');");
                out.println("location.href='index.jsp';"); 
            } else {
                out.println("alert('회원가입에 실패했습니다. 사번이나 아이디가 중복되었는지 확인하세요.');");
                out.println("history.back();");
            }

        } catch (NumberFormatException e) {
            // 사번에 숫자가 아닌 값이 들어왔을 때의 예외 처리입니다.
            out.println("alert('사번은 반드시 숫자로 입력해야 합니다.');");
            out.println("history.back();");
        } catch (Exception e) {
            // 그 외 데이터베이스 연결 오류 등 일반적인 예외 처리입니다.
            e.printStackTrace();
            out.println("alert('처리 중 오류가 발생했습니다.');");
            out.println("history.back();");
        }
        
        out.println("</script>");
        out.flush();
        out.close();
    }
}