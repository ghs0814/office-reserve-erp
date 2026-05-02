<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.office.dto.RentalHistoryDTO"%>
<%@ page import="com.office.dto.ReservationDTO"%>
<%@ page import="com.office.dto.EmployeeDTO"%>
<%
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    if (loginEmp == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    List<RentalHistoryDTO> myList = (List<RentalHistoryDTO>) request.getAttribute("myList");
    List<ReservationDTO> reserveList = (List<ReservationDTO>) request.getAttribute("reserveList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사내 시스템 - 마이페이지</title>
<style>
    body { font-family: 'Segoe UI', 'Malgun Gothic', sans-serif; background-color: #f4f6f9; margin: 0; padding: 40px; }
    .container { max-width: 1100px; margin: 0 auto; }
    
    .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 40px; border-bottom: 2px solid #343a40; padding-bottom: 15px; }
    .page-header h2 { margin: 0; color: #212529; font-size: 24px; }
    .btn-group a { padding: 10px 18px; text-decoration: none; border-radius: 4px; font-weight: bold; font-size: 13px; margin-left: 10px; transition: all 0.2s; }
    .btn-pw { background-color: #ffffff; color: #343a40; border: 1px solid #ced4da; }
    .btn-pw:hover { background-color: #e9ecef; }
    .btn-main { background-color: #343a40; color: #ffffff; border: 1px solid #343a40; }
    .btn-main:hover { background-color: #212529; }

    .section-title { font-size: 18px; font-weight: bold; margin: 0 0 15px 0; color: #343a40; display: flex; align-items: center; }
    .section-title::before { content: ''; display: inline-block; width: 4px; height: 18px; background-color: #6c757d; margin-right: 10px; }

    .table-wrapper { background-color: #ffffff; border-radius: 6px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); margin-bottom: 50px; overflow: hidden; border: 1px solid #e9ecef; }
    table { width: 100%; border-collapse: collapse; text-align: center; font-size: 14px; }
    th, td { padding: 14px 12px; border-bottom: 1px solid #e9ecef; }
    th { background-color: #f8f9fa; color: #495057; font-weight: 600; border-bottom: 2px solid #dee2e6; }
    tr:last-child td { border-bottom: none; }
    tr:hover { background-color: #f8f9fa; }

    .status-badge { padding: 5px 10px; border-radius: 4px; font-weight: bold; font-size: 12px; display: inline-block; }
    .bg-warning { background-color: #fff8e1; color: #f57c00; border: 1px solid #ffe0b2; }
    .bg-success { background-color: #e8f5e9; color: #2e7d32; border: 1px solid #c8e6c9; }
    .bg-danger { background-color: #ffebee; color: #c62828; border: 1px solid #ffcdd2; }
    .bg-primary { background-color: #e3f2fd; color: #1565c0; border: 1px solid #bbdefb; }
    .bg-secondary { background-color: #f1f3f5; color: #495057; border: 1px solid #dee2e6; }

    .btn-action { padding: 6px 14px; border-radius: 4px; cursor: pointer; font-weight: bold; font-size: 12px; transition: all 0.2s; }
    .btn-return { background-color: #343a40; color: #ffffff; border: 1px solid #343a40; }
    .btn-return:hover { background-color: #212529; }
    .btn-cancel { background-color: #ffffff; color: #dc3545; border: 1px solid #dc3545; }
    .btn-cancel:hover { background-color: #dc3545; color: #ffffff; }
</style>
<script>
    function processReturn(rentalNo) {
        if (confirm("해당 비품을 반납 처리하시겠습니까?")) {
            location.href = 'returnProcess.do?rentalNo=' + rentalNo + '&from=mypage';
        }
    }
    function cancelReserve(resNo) {
        if (confirm("정말 이 예약을 취소하시겠습니까?")) {
            location.href = "cancelReserve.do?resNo=" + resNo + '&from=mypage';
        }
    }
</script>
</head>
<body>

<div class="container">
    <div class="page-header">
        <h2>마이페이지</h2>
        <div class="btn-group">
            <a href="changePw.jsp" class="btn-pw">비밀번호 변경</a>
            <a href="main.jsp" class="btn-main">시스템 메인으로</a>
        </div>
    </div>

    <div class="section-title">내 회의실 예약 현황</div>
    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>예약 번호</th><th>회의실</th><th>예약 일자</th><th>사용 시간</th><th>사용 목적</th><th>상태</th><th>비고</th>
                </tr>
            </thead>
            <tbody>
                <% if (reserveList == null || reserveList.isEmpty()) { %>
                    <tr><td colspan="7" style="padding: 40px; color: #6c757d;">예약 내역이 없습니다.</td></tr>
                <% } else { 
                    for (ReservationDTO dto : reserveList) {
                        String statusClass = "bg-primary";
                        if ("취소됨".equals(dto.getStatus())) statusClass = "bg-danger";
                %>
                    <tr>
                        <td style="color: #6c757d;"><%= dto.getResNo() %></td>
                        <td style="font-weight: 600; color: #343a40;"><%= dto.getRoomId() %>호</td>
                        <td><%= dto.getResDate() %></td>
                        <td><%= dto.getStartTime() %> ~ <%= dto.getEndTime() %></td>
                        <td><%= dto.getPurpose() %></td>
                        <td><span class="status-badge <%= statusClass %>"><%= dto.getStatus() %></span></td>
                        <td>
                            <% if ("예약완료".equals(dto.getStatus())) { %>
                                <button class="btn-action btn-cancel" onclick="cancelReserve(<%= dto.getResNo() %>)">예약 취소</button>
                            <% } else { %> <span style="color: #ced4da;">-</span> <% } %>
                        </td>
                    </tr>
                <%  } } %>
            </tbody>
        </table>
    </div>

    <div class="section-title">내 비품 대여 현황</div>
    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>기안 번호</th><th>기안 제목</th><th>비품명</th><th>대여 기간</th><th>상태</th><th>비고</th>
                </tr>
            </thead>
            <tbody>
                <% if (myList == null || myList.isEmpty()) { %>
                    <tr><td colspan="6" style="padding: 40px; color: #6c757d;">비품 대여 기안 내역이 없습니다.</td></tr>
                <% } else { 
                    for (RentalHistoryDTO item : myList) {
                        String status = item.getStatus();
                        String badgeClass = "bg-secondary"; 
                        if ("승인대기".equals(status)) badgeClass = "bg-warning";
                        else if ("대여중".equals(status)) badgeClass = "bg-success";
                        else if ("반려됨".equals(status)) badgeClass = "bg-danger";
                %>
                    <tr>
                        <td style="color: #6c757d;"><%= item.getRentalNo() %></td>
                        <td style="text-align: left; padding-left: 20px;"><%= item.getTitle() != null ? item.getTitle() : "제목 없음" %></td>
                        <td style="font-weight: 600; color: #343a40;"><%= item.getEqName() %></td>
                        <td><%= item.getRentalDate() %> ~ <%= item.getReturnDate() %></td>
                        <td><span class="status-badge <%= badgeClass %>"><%= status %></span></td>
                        <td>
                            <% if ("대여중".equals(status)) { %>
                                <button class="btn-action btn-return" onclick="processReturn('<%= item.getRentalNo() %>')">반납 처리</button>
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