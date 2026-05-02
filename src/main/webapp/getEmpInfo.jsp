<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.office.dao.EmployeeDAO" %>
<%@ page import="com.office.dto.EmployeeDTO" %>
<%
    // 자바스크립트가 보낸 사번을 받습니다.
    String empNo = request.getParameter("empNo");
    
    EmployeeDAO dao = new EmployeeDAO();
    EmployeeDTO dto = dao.getEmployeeByNo(empNo);

    // 자바스크립트가 알아들을 수 있는 JSON 형태로 텍스트를 만들어서 보냅니다.
    if(dto != null) {
        String json = String.format("{\"result\":\"success\", \"empName\":\"%s\", \"empLevel\":%d}", dto.getEmpName(), dto.getEmpLevel());
        out.print(json);
    } else {
        out.print("{\"result\":\"fail\"}");
    }
%>