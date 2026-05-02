<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.office.dto.RentalHistoryDTO" %>
<%@ page import="com.office.dto.EmployeeDTO" %>
<%
    List<RentalHistoryDTO> docList = (List<RentalHistoryDTO>) request.getAttribute("docList");
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>사내 시스템 - 기안 문서함</title>
    <style>
        body { font-family: 'Segoe UI', 'Malgun Gothic', sans-serif; background-color: #f4f6f9; padding: 40px; margin: 0; }
        .container { max-width: 1200px; margin: 0 auto; }
        
        .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; border-bottom: 2px solid #343a40; padding-bottom: 15px; }
        .page-header h2 { margin: 0; color: #212529; font-size: 22px; }
        
        .btn-back { padding: 10px 18px; background-color: #ffffff; color: #343a40; text-decoration: none; border: 1px solid #ced4da; border-radius: 4px; font-weight: bold; font-size: 13px; transition: 0.2s; }
        .btn-back:hover { background-color: #e9ecef; }

        .table-wrapper { background-color: #ffffff; border-radius: 6px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); overflow: hidden; border: 1px solid #e9ecef; }
        table { width: 100%; border-collapse: collapse; text-align: center; font-size: 14px; }
        th, td { padding: 14px 12px; border-bottom: 1px solid #e9ecef; }
        th { background-color: #f8f9fa; color: #495057; font-weight: 600; border-bottom: 2px solid #dee2e6; }
        tr:last-child td { border-bottom: none; }
        tr:hover { background-color: #f8f9fa; }
        
        .clickable-link { color: #212529; text-decoration: underline; cursor: pointer; font-weight: 600; transition: color 0.2s; }
        .clickable-link:hover { color: #495057; }
        .non-clickable { color: #adb5bd; }
        
        .status-badge { padding: 5px 10px; border-radius: 4px; font-weight: bold; font-size: 12px; display: inline-block; }
        .status-ing { background-color: #f8f9fa; color: #495057; border: 1px solid #ced4da; }
        .status-done { background-color: #e9ecef; color: #212529; border: 1px solid #adb5bd; }
        .status-reject { background-color: #ffebee; color: #c62828; border: 1px solid #ffcdd2; }
    </style>
</head>
<body>
<div class="container">
    <div class="page-header">
        <h2>전사 기안 문서함 (전체 현황)</h2>
        <a href="main.jsp" class="btn-back">시스템 메인으로</a>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>문서 번호</th>
                    <th>기안 일자</th>
                    <th>완료 일자</th>
                    <th>기안 제목</th>
                    <th>기안자 사번</th>
                    <th>기안자 성명</th>
                    <th>기안자 직급</th>
                    <th>결재 상태</th>
                </tr>
            </thead>
            <tbody>
                <%
                if (docList != null && !docList.isEmpty()) {
                    for (RentalHistoryDTO doc : docList) {
                        boolean canView = false;
                        if (loginEmp != null) {
                            if ((loginEmp.getEmpNo() == doc.getEmpNo()) || (loginEmp.getEmpLevel() > doc.getEmpLevel())) {
                                canView = true;
                            }
                        }

                        String finishDate = (doc.getSign5Date() != null) ? doc.getSign5Date().toString() : "-";
                        String docTitle = (doc.getTitle() != null && !doc.getTitle().isEmpty()) ? doc.getTitle() : "제목 없음";

                        String statusClass = "status-ing"; 
                        if ("대여중".equals(doc.getStatus()) || "반납완료".equals(doc.getStatus())) {
                            statusClass = "status-done"; 
                        } else if ("반려됨".equals(doc.getStatus())) {
                            statusClass = "status-reject"; 
                        }
                %>
                    <tr>
                        <td style="color: #6c757d;"><%= doc.getRentalNo() %></td>
                        <td><%= doc.getSign1Date() != null ? doc.getSign1Date() : doc.getRentalDate() %></td>
                        <td style="color: #6c757d;"><%= finishDate %></td>
                        
                        <td style="text-align: left; padding-left: 20px;">
                            <% if (canView) { %>
                                <a href="documentDetail.do?rentalNo=<%= doc.getRentalNo() %>" class="clickable-link">
                                    <%= docTitle %>
                                </a>
                            <% } else { %>
                                <span class="non-clickable"><%= docTitle %> <br><small>(열람 권한 없음)</small></span>
                            <% } %>
                        </td>

                        <td style="color: #6c757d;"><%= doc.getEmpNo() %></td>
                        <td><%= doc.getEmpName() %></td>
                        <td><%= doc.getEmpLevel() %>단계</td>
                        
                        <td>
                            <% if (canView) { %>
                                <a href="approvalStatus.do?rentalNo=<%= doc.getRentalNo() %>" class="status-badge <%= statusClass %>" style="text-decoration:none; color:inherit;">
                                    <%= doc.getStatus() %>
                                </a>
                            <% } else { %>
                                <span class="status-badge <%= statusClass %>"><%= doc.getStatus() %></span>
                            <% } %>
                        </td>
                    </tr>
                <%
                    }
                } else {
                %>
                    <tr><td colspan="8" style="padding: 40px; color: #6c757d;">현재 등록된 기안 문서가 없습니다.</td></tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>