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

@WebServlet("/insertEmp.do")
public class AdminEmpInsertController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 1. 관리자가 폼에서 넘긴 파라미터 수집
        int empNo = Integer.parseInt(request.getParameter("empNo"));
        String empName = request.getParameter("empName");
        int empLevel = Integer.parseInt(request.getParameter("empLevel"));
        String manager = request.getParameter("manager");

        // 2. DTO에 담기 (비밀번호는 세팅하지 않음)
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmpNo(empNo);
        dto.setEmpName(empName);
        dto.setEmpLevel(empLevel);
        dto.setManager(manager);

        // 3. DAO 호출하여 DB에 Insert
        EmployeeDAO dao = new EmployeeDAO();
        boolean isSuccess = dao.insertEmployee(dto);

        PrintWriter out = response.getWriter();
        out.println("<script>");
        if (isSuccess) {
            out.println("alert('신규 사원 정보가 시스템에 사전 등록되었습니다.');");
            out.println("location.href='admin.do';"); // 사원 관리 목록 페이지로 리다이렉트
        } else {
            out.println("alert('등록에 실패했습니다. 이미 존재하는 사번인지 확인해 주세요.');");
            out.println("history.back();");
        }
        out.println("</script>");
        out.flush();
        out.close();
    }
}