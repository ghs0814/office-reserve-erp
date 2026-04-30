<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.office.dto.EmployeeDTO" %>
<%
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    if (loginEmp == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 메인 대시보드</title>
<style>
    body {
        font-family: 'Malgun Gothic', sans-serif;
        background-color: #f0f2f5;
        margin: 0;
        padding: 20px;
    }
    
    .header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        background-color: white;
        padding: 15px 30px;
        border-radius: 8px;
        box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        margin-bottom: 20px;
    }
    
    .logout-btn {
        padding: 8px 15px;
        background-color: #ff4c4c;
        color: white;
        text-decoration: none;
        border-radius: 4px;
        font-weight: bold;
    }
    
    .map-container {
        position: relative; 
        width: 1000px;       
        height: 550px; 
        background-color: white;
        margin: 0 auto;
        border: 2px solid #ccc;
        background-image: url('images/floor4_map.png'); 
        background-size: 100% 100%;
        background-repeat: no-repeat;
    }
    
    .room-btn {
        position: absolute; 
        background-color: transparent; 
        border: none;                  
        cursor: pointer;
        display: flex;
        justify-content: center;
        align-items: center;
        font-weight: bold;
        color: transparent; 
        transition: 0.2s;
        box-sizing: border-box;
    }
    
    .room-btn:hover {
        background-color: rgba(76, 175, 80, 0.4); 
        border: 2px solid #4CAF50; 
        color: white; 
    }
    
    #room404 { top: 4%; left: 4%; width: 24%; height: 34%; }
    #room403 { top: 4%; left: 29%; width: 24%; height: 34%; }
    #room402 { top: 4%; left: 53%; width: 19%; height: 34%; }
    #room401 { top: 4%; left: 73%; width: 20%; height: 34%; }
    
    #roomInterview { top: 56%; left: 38%; width: 13%; height: 33%; }
    #roomConsult { top: 56%; left: 51%; width: 15%; height: 33%; }
    #roomMeeting { top: 39%; left: 81%; width: 12%; height: 49%; }

</style>
</head>
<body>

    <div class="header">
        <h2>오피스 예약 시스템</h2>
        <div>
            <b><%= loginEmp.getEmpName() %></b>님 환영합니다.
            <!-- '내 예약 조회' 버튼 추가 -->
            <a href="myReserveList.do" class="logout-btn" style="background-color: #2196F3; margin-right: 10px;">내 예약 조회</a>
            <a href="logout.do" class="logout-btn">로그아웃</a>
        </div>
    </div>

    <div class="map-container">
        <div class="room-btn" id="room404" onclick="location.href='reserve.do?roomId=404'"></div>
        <div class="room-btn" id="room403" onclick="location.href='reserve.do?roomId=403'"></div>
        <div class="room-btn" id="room402" onclick="location.href='reserve.do?roomId=402'"></div>
        <div class="room-btn" id="room401" onclick="location.href='reserve.do?roomId=401'"></div>
        
        <div class="room-btn" id="roomInterview" onclick="location.href='reserve.do?roomId=Interview'"></div>
        <div class="room-btn" id="roomConsult" onclick="location.href='reserve.do?roomId=Consult'"></div>
        <div class="room-btn" id="roomMeeting" onclick="location.href='reserve.do?roomId=Meeting'"></div>
    </div>

</body>
</html>