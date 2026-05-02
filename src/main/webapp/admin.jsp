<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.office.dto.EmployeeDTO" %>
<%
    // 자바 코드를 사용하여 request 영역에 담긴 사원 목록과 로그인 정보를 꺼냅니다.
    List<EmployeeDTO> empList = (List<EmployeeDTO>) request.getAttribute("empList");
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>사원 관리 페이지 (관리자 전용)</title>
    <style>
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ccc; padding: 10px; text-align: center; }
        th { background-color: #eee; }
    </style>
</head>
<body>
    <h2>사원 관리 페이지 (최고 관리자 전용)</h2>
    <a href="index.jsp">메인 화면으로 돌아가기</a>

    <table>
        <thead>
            <tr>
                <th>사번</th>
                <th>이름</th>
                <th>직급</th>
                <th>관리자 여부</th>
                <th>관리 기능</th>
            </tr>
        </thead>
        <tbody>
            <% 
            // for문을 사용하여 리스트의 데이터를 하나씩 꺼내어 HTML 표를 생성합니다.
            if(empList != null) {
                for(EmployeeDTO emp : empList) { 
            %>
                <tr>
                    <td><%= emp.getEmpNo() %></td>
                    <td><%= emp.getEmpName() %></td>
                    
                    <!-- 직급 변경 영역 -->
                    <td>
                        <form action="adminAction.do" method="post" style="margin:0;">
                            <input type="hidden" name="action" value="updateLevel">
                            <input type="hidden" name="empNo" value="<%= emp.getEmpNo() %>">
                            <select name="newLevel">
                                <option value="1" <%= emp.getEmpLevel() == 1 ? "selected" : "" %>>1단계</option>
                                <option value="2" <%= emp.getEmpLevel() == 2 ? "selected" : "" %>>2단계</option>
                                <option value="3" <%= emp.getEmpLevel() == 3 ? "selected" : "" %>>3단계</option>
                                <option value="4" <%= emp.getEmpLevel() == 4 ? "selected" : "" %>>4단계</option>
                                <option value="5" <%= emp.getEmpLevel() == 5 ? "selected" : "" %>>5단계</option>
                            </select>
                            <button type="submit">변경</button>
                        </form>
                    </td>

                    <td><%= "Y".equals(emp.getManager()) ? "최고 관리자" : "일반 사원" %></td>

                    <!-- 퇴사 및 권한 이양 영역 (나 자신한테는 이 버튼들이 안 보이게 처리) -->
                    <td>
                        <% if(loginEmp != null && emp.getEmpNo() != loginEmp.getEmpNo()) { %>
                            <form action="adminAction.do" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="transferManager">
                                <input type="hidden" name="empNo" value="<%= emp.getEmpNo() %>">
                                <button type="submit" onclick="return confirm('이 사원에게 권한을 완전히 넘기시겠습니까? 본인은 일반 사원이 됩니다.');">권한 넘기기</button>
                            </form>
                            
                            <form action="adminAction.do" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="deleteEmp">
                                <input type="hidden" name="empNo" value="<%= emp.getEmpNo() %>">
                                <button type="submit" onclick="return confirm('정말 이 사원을 퇴사(삭제) 처리하시겠습니까?');">퇴사 처리</button>
                            </form>
                        <% } %>
                    </td>
                </tr>
            <% 
                } 
            } 
            %>
        </tbody>
    </table>
</body>
</html>