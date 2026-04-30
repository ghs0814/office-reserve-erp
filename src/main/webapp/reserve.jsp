<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.office.dto.EmployeeDTO" %>
<%
    // 1. 세션 검사 (로그인 상태 확인)
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    if (loginEmp == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    // 2. ReserveController에서 포워드 방식으로 넘겨준 방 번호 받기
    String roomId = (String) request.getAttribute("selectedRoomId");
    
    // 비정상적인 접근 방지 (방 번호가 없으면 메인으로 돌려보냄)
    if (roomId == null || roomId.trim().isEmpty()) {
        response.sendRedirect("main.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 예약 신청</title>
<style>
    body {
        font-family: 'Malgun Gothic', sans-serif;
        background-color: #f0f2f5;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
    }
    
    .reserve-container {
        background-color: white;
        padding: 40px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        width: 400px;
    }
    
    .reserve-container h2 {
        text-align: center;
        color: #333;
        margin-bottom: 10px;
    }
    
    .room-badge {
        display: block;
        text-align: center;
        color: #4CAF50;
        font-weight: bold;
        font-size: 20px;
        margin-bottom: 30px;
    }
    
    .form-group {
        margin-bottom: 20px;
    }
    
    .form-group label {
        display: block;
        margin-bottom: 8px;
        font-weight: bold;
        color: #555;
        font-size: 14px;
    }
    
    .form-group input[type="date"],
    .form-group input[type="time"],
    .form-group input[type="text"] {
        width: 100%;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 4px;
        box-sizing: border-box;
    }
    
    .btn-group {
        display: flex;
        gap: 10px;
        margin-top: 30px;
    }
    
    .btn-submit {
        flex: 1;
        padding: 12px;
        background-color: #4CAF50;
        color: white;
        border: none;
        border-radius: 4px;
        font-size: 16px;
        cursor: pointer;
        font-weight: bold;
    }
    
    .btn-cancel {
        flex: 1;
        padding: 12px;
        background-color: #ccc;
        color: #333;
        border: none;
        border-radius: 4px;
        font-size: 16px;
        cursor: pointer;
        text-align: center;
        text-decoration: none;
        font-weight: bold;
        box-sizing: border-box;
    }
    
    .btn-submit:hover { background-color: #45a049; }
    .btn-cancel:hover { background-color: #bbb; }
</style>
</head>
<body>

<div class="reserve-container">
    <h2>회의실 예약 신청</h2>
    <!-- 컨트롤러에서 넘겨준 방 번호를 화면에 예쁘게 출력합니다 -->
    <span class="room-badge">[ <%= roomId %> ]</span>
    
    <!-- 예약을 실제 처리할 다음 컨트롤러(reserveProcess.do)로 데이터를 보냅니다 -->
    <form action="reserveProcess.do" method="post">
        
        <!-- DTO 구조에 맞춘 필수 hidden 데이터 -->
        <input type="hidden" name="roomId" value="<%= roomId %>">
        <input type="hidden" name="empNo" value="<%= loginEmp.getEmpNo() %>">
        <input type="hidden" name="status" value="예약완료">
        
        <div class="form-group">
            <label for="resDate">예약 날짜</label>
            <input type="date" id="resDate" name="resDate" required>
        </div>
        
        <div class="form-group">
            <label for="startTime">시작 시간</label>
            <input type="time" id="startTime" name="startTime" required>
        </div>
        
        <div class="form-group">
            <label for="endTime">종료 시간</label>
            <input type="time" id="endTime" name="endTime" required>
        </div>
        
        <div class="form-group">
            <label for="purpose">사용 목적</label>
            <input type="text" id="purpose" name="purpose" placeholder="예) 주간 회의, 클라이언트 미팅 등" required>
        </div>
        
        <div class="btn-group">
            <a href="main.jsp" class="btn-cancel">취소</a>
            <button type="submit" class="btn-submit">예약하기</button>
        </div>
    </form>
</div>

</body>
</html>