<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.office.dto.EmployeeDTO" %>
<%
    List<EmployeeDTO> empList = (List<EmployeeDTO>) request.getAttribute("empList");
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>사내 시스템 - 사원 관리</title>
    <style>
        body { font-family: 'Segoe UI', 'Malgun Gothic', sans-serif; background-color: #f4f6f9; padding: 40px; margin: 0; }
        .container { max-width: 1100px; margin: 0 auto; }
        
        .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; border-bottom: 2px solid #343a40; padding-bottom: 15px; }
        .page-header h2 { margin: 0; color: #212529; font-size: 22px; }
        
        .btn-back { padding: 10px 18px; background-color: #ffffff; color: #343a40; text-decoration: none; border: 1px solid #ced4da; border-radius: 4px; font-weight: bold; font-size: 13px; transition: 0.2s; }
        .btn-back:hover { background-color: #e9ecef; }

        .table-wrapper { background-color: #ffffff; border-radius: 6px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); overflow: hidden; border: 1px solid #e9ecef; }
        table { width: 100%; border-collapse: collapse; text-align: center; font-size: 14px; }
        th, td { padding: 14px 12px; border-bottom: 1px solid #e9ecef; vertical-align: middle; }
        th { background-color: #f8f9fa; color: #495057; font-weight: 600; border-bottom: 2px solid #dee2e6; }
        tr:last-child td { border-bottom: none; }
        tr:hover { background-color: #f8f9fa; }

        select { padding: 6px 10px; border: 1px solid #ced4da; border-radius: 4px; outline: none; font-family: inherit; color: #495057; }
        
        .btn-action { padding: 6px 12px; border-radius: 4px; font-size: 12px; font-weight: bold; cursor: pointer; transition: 0.2s; border: none; }
        .btn-update { background-color: #343a40; color: #ffffff; border: 1px solid #343a40; }
        .btn-update:hover { background-color: #212529; }
        
        .btn-transfer { background-color: #ffffff; color: #f57c00; border: 1px solid #f57c00; margin-right: 5px; }
        .btn-transfer:hover { background-color: #fff3e0; }
        
        .btn-delete { background-color: #ffffff; color: #dc3545; border: 1px solid #dc3545; }
        .btn-delete:hover { background-color: #fff5f5; }

        .badge-manager { background-color: #e3f2fd; color: #1565c0; border: 1px solid #bbdefb; padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: bold; }
        .badge-normal { background-color: #f1f3f5; color: #6c757d; border: 1px solid #dee2e6; padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: bold; }
    </style>
</head>
<body>
<div class="container">
    <div class="page-header">
        <h2>전사 직원 관리 (마스터)</h2>
        <a href="main.jsp" class="btn-back">시스템 메인으로</a>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>사원 번호</th>
                    <th>성명</th>
                    <th>권한 레벨 조정</th>
                    <th>시스템 권한</th>
                    <th>인사 관리 (위임/퇴사)</th>
                </tr>
            </thead>
            <tbody>
                <% if(empList != null) {
                    for(EmployeeDTO emp : empList) { 
                %>
                    <tr>
                        <td style="color: #6c757d;"><%= emp.getEmpNo() %></td>
                        <td style="font-weight: 600; color: #343a40;"><%= emp.getEmpName() %></td>
                        
                        <td>
                            <form action="adminAction.do" method="post" style="margin:0; display:flex; justify-content:center; gap:5px; align-items:center;">
                                <input type="hidden" name="action" value="updateLevel">
                                <input type="hidden" name="empNo" value="<%= emp.getEmpNo() %>">
                                <select name="newLevel">
                                    <option value="1" <%= emp.getEmpLevel() == 1 ? "selected" : "" %>>1단계 (일반)</option>
                                    <option value="2" <%= emp.getEmpLevel() == 2 ? "selected" : "" %>>2단계</option>
                                    <option value="3" <%= emp.getEmpLevel() == 3 ? "selected" : "" %>>3단계</option>
                                    <option value="4" <%= emp.getEmpLevel() == 4 ? "selected" : "" %>>4단계 (부서장)</option>
                                    <option value="5" <%= emp.getEmpLevel() == 5 ? "selected" : "" %>>5단계 (임원)</option>
                                </select>
                                <button type="submit" class="btn-action btn-update">권한 수정</button>
                            </form>
                        </td>

                        <td>
                            <% if ("Y".equals(emp.getManager())) { %>
                                <span class="badge-manager">최고 관리자</span>
                            <% } else { %>
                                <span class="badge-normal">일반 사원</span>
                            <% } %>
                        </td>

                        <td>
                            <% if(loginEmp != null && emp.getEmpNo() != loginEmp.getEmpNo()) { %>
                                <form action="adminAction.do" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="transferManager">
                                    <input type="hidden" name="empNo" value="<%= emp.getEmpNo() %>">
                                    <button type="submit" class="btn-action btn-transfer" onclick="return confirm('해당 사원에게 최고 관리자 권한을 위임하시겠습니까? (본인은 일반 권한으로 강등됩니다)');">마스터 위임</button>
                                </form>
                                
                                <form action="adminAction.do" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="deleteEmp">
                                    <input type="hidden" name="empNo" value="<%= emp.getEmpNo() %>">
                                    <button type="submit" class="btn-action btn-delete" onclick="return confirm('경고: 사원 정보를 시스템에서 영구히 삭제(퇴사 처리)하시겠습니까?');">퇴사 처리</button>
                                </form>
                            <% } else { %>
                                <span style="color: #adb5bd; font-size: 12px;">본인 계정</span>
                            <% } %>
                        </td>
                    </tr>
                <%  } } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>