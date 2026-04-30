<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.office.dto.EmployeeDTO" %>
<%
    // 1. 세션 검사 (로그인 안 한 사람이 주소 치고 들어오는 것 방지)
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    
    if (loginEmp == null) {
        // 로그인 정보가 없으면 다시 로그인 화면으로 쫓아냅니다.
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 4층 도면</title>
<style>
    body {
        font-family: 'Malgun Gothic', sans-serif;
        background-color: #f0f2f5;
        margin: 0;
        padding: 20px;
    }
    
    /* 상단 네비게이션 바 */
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
    
    /* 도면 영역 컨테이너 (여기를 기준으로 투명 버튼을 배치합니다) */
    .map-container {
        position: relative; /* 중요: 투명 버튼들의 기준점이 됩니다 */
        width: 800px;       /* 실제 도면 이미지 크기에 맞춰 나중에 수정하세요 */
        height: 600px;
        background-color: white;
        margin: 0 auto;
        border: 2px solid #ccc;
        background-image: url('images/floor4_map.jpg'); /* 나중에 실제 도면 이미지 경로로 변경 */
        background-size: cover;
    }
    
    /* 투명 예약 버튼 공통 스타일 */
    .room-btn {
        position: absolute; /* map-container 안에서 자유롭게 위치 지정 가능 */
        background-color: rgba(76, 175, 80, 0.4); /* 평소에는 반투명한 녹색 */
        border: 2px solid #4CAF50;
        cursor: pointer;
        display: flex;
        justify-content: center;
        align-items: center;
        font-weight: bold;
        color: #333;
        transition: background-color 0.3s;
    }
    
    .room-btn:hover {
        background-color: rgba(76, 175, 80, 0.8); /* 마우스 올리면 진해짐 */
        color: white;
    }
    
    /* 개별 회의실 위치 및 크기 (나중에 도면에 맞춰 조절) */
    #roomA { top: 50px; left: 50px; width: 150px; height: 100px; }
    #roomB { top: 50px; left: 220px; width: 120px; height: 100px; }
    #roomC { bottom: 50px; right: 50px; width: 200px; height: 150px; }

</style>
</head>
<body>

    <!-- 상단 정보 영역 -->
    <div class="header">
        <h2>오피스 예약 시스템</h2>
        <div>
            <!-- JSP 표현식으로 세션에 저장된 사용자 이름 출력 -->
            <b><%= loginEmp.getEmpName() %></b>님 환영합니다.
            <!-- 로그아웃 기능은 나중에 LogoutController를 만들어서 연결합니다 -->
            <a href="logout.do" class="logout-btn">로그아웃</a>
        </div>
    </div>

    <!-- 4층 평면도 및 예약 버튼 영역 -->
    <div class="map-container">
        
        <!-- 투명 버튼들 (클릭 시 예약 화면이나 팝업으로 이동) -->
        <div class="room-btn" id="roomA" onclick="location.href='reserve.do?roomId=A'">회의실 A</div>
        <div class="room-btn" id="roomB" onclick="location.href='reserve.do?roomId=B'">회의실 B</div>
        <div class="room-btn" id="roomC" onclick="location.href='reserve.do?roomId=C'">대회의실 C</div>
        
    </div>

</body>
</html>