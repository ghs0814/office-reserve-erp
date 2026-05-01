<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.office.dto.ReservationDTO" %>
<%@ page import="com.office.dto.EmployeeDTO" %>
<%
    // 1. 보안 체크: 세션에서 로그인 정보를 가져오고, 없으면 로그인 페이지로 안내합니다.
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    if (loginEmp == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    // 2. Controller에서 전달받은 나의 예약 내역 리스트를 가져옵니다.
    List<ReservationDTO> reserveList = (List<ReservationDTO>) request.getAttribute("reserveList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 내 예약 조회</title>
<style>
    /* 페이지 레이아웃 및 예약 상태별 디자인 설정 */
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
    
    tr:nth-child(even) { background-color: #f9f9f9; }
    tr:hover { background-color: #f1f1f1; }
    
    /* 예약 상태 배지 스타일 */
    .status-badge {
        background-color: #e3f2fd;
        color: #1976d2;
        padding: 5px 10px;
        border-radius: 20px;
        font-weight: bold;
        font-size: 12px;
    }

    /* 취소된 상태일 때의 배지 색상(빨간색 계열) */
    .status-cancel {
        background-color: #ffebee;
        color: #d32f2f;
    }
    
    .btn-cancel-small {
        background-color: #f44336;
        color: white;
        border: none;
        padding: 6px 12px;
        border-radius: 4px;
        cursor: pointer;
        font-size: 12px;
        font-weight: bold;
    }
    
    .btn-cancel-small:hover { background-color: #d32f2f; }
</style>

<!-- 3. 예약 취소 확인 및 요청 함수 -->
<script>
    function cancelReserve(resNo) {
        if (confirm("정말 이 예약을 취소하시겠습니까?")) {
            // 확인 시 취소 컨트롤러(CancelReserveController)로 예약 번호를 전송합니다.
            location.href = "cancelReserve.do?resNo=" + resNo;
        }
    }
</script>
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
                <th>비고</th> 
            </tr>
        </thead>
        <tbody>
            <% 
               // 4. 예약 내역이 비어있을 때 처리
               if (reserveList == null || reserveList.isEmpty()) { 
            %>
                <tr>
                    <td colspan="7">예약 내역이 없습니다.</td>
                </tr>
            <% 
               } else { 
                // 5. 내 예약 리스트를 반복하여 테이블 행으로 출력합니다.
                for (ReservationDTO dto : reserveList) {
            %>
                <tr>
                    <td><%= dto.getResNo() %></td>
                    <td><b><%= dto.getRoomId() %></b></td>
                    <td><%= dto.getResDate() %></td>
                    <td><%= dto.getStartTime() %> ~ <%= dto.getEndTime() %></td>
                    <td><%= dto.getPurpose() %></td>
                    <td>
                        <!-- 6. 상태가 '취소됨'인 경우 별도의 클래스(status-cancel)를 적용하여 시각적으로 구분합니다. -->
                        <span class="status-badge <%= "취소됨".equals(dto.getStatus()) ? "status-cancel" : "" %>">
                            <%= dto.getStatus() %>
                        </span>
                    </td>
                    <td>
                        <!-- 7. '예약완료' 상태일 때만 취소 버튼을 노출합니다. -->
                        <% if ("예약완료".equals(dto.getStatus())) { %>
                            <button type="button" class="btn-cancel-small" onclick="cancelReserve(<%= dto.getResNo() %>)">취소</button>
                        <% } else { %>
                            -
                        <% } %>
                    </td>
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