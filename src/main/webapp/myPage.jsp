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

    // 컨트롤러에서 넘어온 두 개의 리스트 받기
    List<RentalHistoryDTO> myList = (List<RentalHistoryDTO>) request.getAttribute("myList");
    List<ReservationDTO> reserveList = (List<ReservationDTO>) request.getAttribute("reserveList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 마이페이지</title>
<style>
    body { font-family: 'Malgun Gothic', sans-serif; background-color: #f0f2f5; margin: 0; padding: 20px; }
    .container { background-color: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); width: 1000px; margin: 0 auto; }
    
    .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; border-bottom: 2px solid #333; padding-bottom: 15px; }
    .btn-group a { padding: 8px 15px; text-decoration: none; border-radius: 4px; font-weight: bold; margin-left: 10px; }
    .btn-pw { background-color: #673AB7; color: white; }
    .btn-main { background-color: #555; color: white; }

    .section-title { font-size: 20px; font-weight: bold; margin: 30px 0 15px 0; color: #333; border-left: 4px solid #4CAF50; padding-left: 10px; }

    table { width: 100%; border-collapse: collapse; text-align: center; font-size: 14px; margin-bottom: 20px; }
    th, td { padding: 12px; border: 1px solid #ddd; }
    th { background-color: #f5f5f5; color: #333; font-weight: bold; }
    tr:nth-child(even) { background-color: #f9f9f9; }
    tr:hover { background-color: #f1f1f1; }

    /* 배지 및 버튼 디자인 (기존 설정 유지) */
    .status-badge { padding: 5px 10px; border-radius: 20px; font-weight: bold; font-size: 12px; display: inline-block; }
    .bg-warning { background-color: #fff3cd; color: #856404; border: 1px solid #ffeeba; }
    .bg-success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
    .bg-secondary { background-color: #e2e3e5; color: #383d41; border: 1px solid #d6d8db; }
    .bg-danger { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
    .bg-primary { background-color: #cce5ff; color: #004085; border: 1px solid #b8daff; }

    .btn-action { padding: 6px 12px; color: white; border: none; border-radius: 4px; cursor: pointer; font-weight: bold; font-size: 12px; }
    .btn-return { background-color: #2196F3; }
    .btn-cancel { background-color: #f44336; }
</style>
<script>
    // 비품 반납 로직
    function processReturn(rentalNo) {
        if (confirm("해당 비품을 반납하시겠습니까?")) {
            // 반납 완료 후 마이페이지로 다시 돌아오도록 변경
            location.href = 'returnProcess.do?rentalNo=' + rentalNo + '&from=mypage';
        }
    }
    // 예약 취소 로직
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
        <h2 style="margin: 0;">마이페이지</h2>
        <div class="btn-group">
            <a href="changePw.jsp" class="btn-pw">비밀번호 변경</a>
            <a href="main.jsp" class="btn-main">메인으로 돌아가기</a>
        </div>
    </div>

    <!-- 1. 회의실 예약 내역 섹션 -->
    <div class="section-title">내 회의실 예약 내역</div>
    <table>
        <thead>
            <tr>
                <th>예약 번호</th><th>회의실</th><th>예약 날짜</th><th>사용 시간</th><th>사용 목적</th><th>상태</th><th>액션</th>
            </tr>
        </thead>
        <tbody>
            <% if (reserveList == null || reserveList.isEmpty()) { %>
                <tr><td colspan="7">예약 내역이 없습니다.</td></tr>
            <% } else { 
                for (ReservationDTO dto : reserveList) {
                    String statusClass = "bg-primary";
                    if ("취소됨".equals(dto.getStatus())) statusClass = "bg-danger";
            %>
                <tr>
                    <td><%= dto.getResNo() %></td>
                    <td><b><%= dto.getRoomId() %></b></td>
                    <td><%= dto.getResDate() %></td>
                    <td><%= dto.getStartTime() %> ~ <%= dto.getEndTime() %></td>
                    <td><%= dto.getPurpose() %></td>
                    <td><span class="status-badge <%= statusClass %>"><%= dto.getStatus() %></span></td>
                    <td>
                        <% if ("예약완료".equals(dto.getStatus())) { %>
                            <button class="btn-action btn-cancel" onclick="cancelReserve(<%= dto.getResNo() %>)">취소</button>
                        <% } else { %> - <% } %>
                    </td>
                </tr>
            <%  } } %>
        </tbody>
    </table>

    <!-- 2. 비품 대여 내역 섹션 -->
    <div class="section-title" style="margin-top: 50px;">내 비품 대여 내역</div>
    <table>
        <thead>
            <tr>
                <th>기안 번호</th><th>기안 제목</th><th>비품명</th><th>대여 기간</th><th>상태</th><th>액션</th>
            </tr>
        </thead>
        <tbody>
            <% if (myList == null || myList.isEmpty()) { %>
                <tr><td colspan="6">비품 대여 기안 내역이 없습니다.</td></tr>
            <% } else { 
                for (RentalHistoryDTO item : myList) {
                    String status = item.getStatus();
                    String badgeClass = "bg-secondary"; 
                    if ("승인대기".equals(status)) badgeClass = "bg-warning";
                    else if ("대여중".equals(status)) badgeClass = "bg-success";
                    else if ("반려됨".equals(status)) badgeClass = "bg-danger";
            %>
                <tr>
                    <td><%= item.getRentalNo() %></td>
                    <td><%= item.getTitle() != null ? item.getTitle() : "제목 없음" %></td>
                    <td><b><%= item.getEqName() %></b></td>
                    <td><%= item.getRentalDate() %> ~ <%= item.getReturnDate() %></td>
                    <td><span class="status-badge <%= badgeClass %>"><%= status %></span></td>
                    <td>
                        <% if ("대여중".equals(status)) { %>
                            <button class="btn-action btn-return" onclick="processReturn('<%= item.getRentalNo() %>')">반납</button>
                        <% } else { %> - <% } %>
                    </td>
                </tr>
            <%  } } %>
        </tbody>
    </table>
</div>

</body>
</html>