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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

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

		EmployeeDAO dao = new EmployeeDAO();

		// ★ 수정된 부분: 세션 비밀번호 대신, DB에 직접 로그인 체크 로직을 돌려 현재 비밀번호 검증
		EmployeeDTO checkEmp = dao.loginCheck(String.valueOf(loginEmp.getEmpNo()), currentPw);

		if (checkEmp == null) {
			// 비밀번호가 틀렸을 경우
			out.println("alert('현재 비밀번호가 일치하지 않습니다.');");
			out.println("history.back();");
		} else {
			// 비밀번호가 맞았을 경우 -> 업데이트 로직 실행
			// (참고: DAO에 만들어두신 비밀번호 업데이트 메서드명에 맞게 사용하세요)
			boolean isSuccess = dao.updateEmployeePassword(String.valueOf(loginEmp.getEmpNo()), newPw);

			if (isSuccess) {
				session.invalidate();
				out.println("alert('비밀번호가 성공적으로 변경되었습니다. 다시 로그인해주세요.');");
				out.println("location.href='index.jsp';");
			} else {
				out.println("alert('비밀번호 변경에 실패했습니다.');");
				out.println("history.back();");
			}
		}

		out.println("</script>");
		out.flush();
		out.close();
	}
}