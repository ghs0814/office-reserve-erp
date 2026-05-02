package com.office.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.office.dao.EmployeeDAO;
import com.office.dto.EmployeeDTO;

/**
 * 관리자 페이지(사원 목록 조회)를 처리하는 컨트롤러입니다.
 */
@WebServlet("/admin.do")
public class AdminController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");

        // 방어 로직: 로그인을 안 했거나, 관리자(manager='Y')가 아니면 튕겨냅니다.
        if (loginEmp == null || !"Y".equals(loginEmp.getManager())) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('관리자만 접근할 수 있는 페이지입니다.'); location.href='index.jsp';</script>");
            return;
        }

        // DAO를 통해 전체 사원 목록을 가져옵니다.
        EmployeeDAO dao = new EmployeeDAO();
        List<EmployeeDTO> empList = dao.getAllEmployees();

        // request 영역에 데이터를 담고 JSP로 포워딩합니다.
        request.setAttribute("empList", empList);
        request.getRequestDispatcher("admin.jsp").forward(request, response);
    }
}