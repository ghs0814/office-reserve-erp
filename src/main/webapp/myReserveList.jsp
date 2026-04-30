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

    // 컨트롤러에서 넘겨준 예약 리스트 받기
    List<ReservationDTO> reserveList = (List<ReservationDTO>) request.getAttribute("reserveList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 내 예약 조회</title>
<style>
    body {
        font-family: 'Malgun Gothic', sans-serif;
        background-color: #f0f2f5;
        margin: 0;
        padding: 20px;
    }
    
    .container {
        background-color: white;
        padding: 30px;
        border-radius: 8px;
        box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        width: 900px;
        margin: 0 auto;
    }
    
    .page-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
        border-bottom: 2px solid #eee;
        padding-bottom: 10px;
    }
    
    .btn-back {
        padding: 8px 15px;
        background-color: #555;
        color: white;
        text-decoration: none;
        border-radius: 4px;
        font-weight: bold;
    }
    
    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
    }
    
    th, td {
        border: 1px solid #ddd;
        padding: 12px;
        text-align: center;
    }
    
    th {
        background-color: #4CAF50;
        color: white;
    }
    
    tr:nth-child(even) {
        background-color: #f9f9f9;
    }
    
    tr:hover {
        background-color: #f1f1f1;
    }
    
    .status-badge {
        background-color: #e3f2fd;
        color: #1976d2;
        padding: 5px 10px;
        border-radius: 20px;
        font-weight: bold;
        font-size: 12px;
    }
</style>
</head>
<body>

<div class="container">
    <div class="page-header">
        <h2>나의 예약 내역</h2>
        <a href="main.jsp" class="btn-back">메인으로 돌아가기</a>
    </div>

    <table>
        <thead>
            <tr>
                <th>예약 번호</th>
                <th>방 번호</th>
                <th>예약 날짜</th>
                <th>사용 시간</th>
                <th>사용 목적</th>
                <th>상태</th>
            </tr>
        </thead>
        <tbody>
            <% if (reserveList == null || reserveList.isEmpty()) { %>
                <tr>
                    <td colspan="6">예약 내역이 없습니다.</td>
                </tr>
            <% } else { 
                for (ReservationDTO dto : reserveList) {
            %>
                <tr>
                    <td><%= dto.getResNo() %></td>
                    <td><b><%= dto.getRoomId() %></b></td>
                    <td><%= dto.getResDate() %></td>
                    <td><%= dto.getStartTime() %> ~ <%= dto.getEndTime() %></td>
                    <td><%= dto.getPurpose() %></td>
                    <td><span class="status-badge"><%= dto.getStatus() %></span></td>
                </tr>
            <% 
                } 
            } 
            %>
        </tbody>
    </table>
</div>

</body>
</html>