<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.office.dto.ReservationDTO" %>
<%@ page import="com.office.dto.EmployeeDTO" %>
<%
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    if (loginEmp == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    List<ReservationDTO> reserveList = (List<ReservationDTO>) request.getAttribute("reserveList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사내 시스템 - 내 예약 조회</title>
<style>
    body { font-family: 'Segoe UI', 'Malgun Gothic', sans-serif; background-color: #f4f6f9; margin: 0; padding: 40px; }
    .container { max-width: 1000px; margin: 0 auto; }
    
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
    
    .status-badge { padding: 5px 10px; border-radius: 4px; font-weight: bold; font-size: 12px; display: inline-block; }
    .bg-primary { background-color: #e3f2fd; color: #1565c0; border: 1px solid #bbdefb; }
    .bg-danger { background-color: #ffebee; color: #c62828; border: 1px solid #ffcdd2; }
    
    .btn-cancel-small { background-color: #ffffff; color: #dc3545; border: 1px solid #dc3545; padding: 6px 14px; border-radius: 4px; cursor: pointer; font-size: 12px; font-weight: bold; transition: 0.2s; }
    .btn-cancel-small:hover { background-color: #dc3545; color: #ffffff; }
</style>
<script>
    function cancelReserve(resNo) {
        if (confirm("정말 이 예약을 취소하시겠습니까?")) {
            location.href = "cancelReserve.do?resNo=" + resNo;
        }
    }
</script>
</head>
<body>

<div class="container">
    <div class="page-header">
        <h2>나의 예약 전체 내역</h2>
        <a href="main.jsp" class="btn-back">시스템 메인으로</a>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>예약 번호</th><th>회의실</th><th>예약 날짜</th><th>사용 시간</th><th>사용 목적</th><th>상태</th><th>비고</th> 
                </tr>
            </thead>
            <tbody>
                <% if (reserveList == null || reserveList.isEmpty()) { %>
                    <tr><td colspan="7" style="padding: 40px; color: #6c757d;">예약 내역이 없습니다.</td></tr>
                <% } else { 
                    for (ReservationDTO dto : reserveList) {
                %>
                    <tr>
                        <td style="color: #6c757d;"><%= dto.getResNo() %></td>
                        <td style="font-weight: 600; color: #343a40;"><%= dto.getRoomId() %>호</td>
                        <td><%= dto.getResDate() %></td>
                        <td><%= dto.getStartTime() %> ~ <%= dto.getEndTime() %></td>
                        <td><%= dto.getPurpose() %></td>
                        <td>
                            <span class="status-badge <%= "취소됨".equals(dto.getStatus()) ? "bg-danger" : "bg-primary" %>">
                                <%= dto.getStatus() %>
                            </span>
                        </td>
                        <td>
                            <% if ("예약완료".equals(dto.getStatus())) { %>
                                <button type="button" class="btn-cancel-small" onclick="cancelReserve(<%= dto.getResNo() %>)">예약 취소</button>
                            <% } else { %> <span style="color: #ced4da;">-</span> <% } %>
                        </td>
                    </tr>
                <%  } } %>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>