<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.office.dto.EmployeeDTO"%>
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
<title>사내 시스템 - 대시보드</title>
<style>
body {
	font-family: 'Segoe UI', 'Malgun Gothic', sans-serif;
	background-color: #f4f6f9;
	margin: 0;
	padding: 30px;
}
.header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	background-color: #212529; /* 다크 테마 헤더 */
	color: #ffffff;
	padding: 15px 30px;
	border-radius: 8px;
	box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
	margin-bottom: 30px;
}
.header h2 {
    margin: 0;
    font-size: 20px;
    letter-spacing: 1px;
}
.nav-buttons {
    display: flex;
    align-items: center;
    gap: 10px;
}
.nav-btn {
	padding: 8px 16px;
	background-color: #343a40;
	color: #ffffff;
	text-decoration: none;
	border-radius: 4px;
	font-weight: 600;
    font-size: 13px;
    border: 1px solid #495057;
    transition: all 0.2s;
}
.nav-btn:hover { background-color: #495057; border-color: #6c757d; }
.nav-btn.admin { border-color: #dc3545; color: #ffc107; }
.nav-btn.admin:hover { background-color: #dc3545; color: #ffffff; }
.nav-btn.logout { background-color: transparent; border: none; color: #adb5bd; text-decoration: underline; }
.nav-btn.logout:hover { color: #ffffff; }

.map-wrapper {
    background-color: #ffffff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.05);
    border: 1px solid #e9ecef;
    display: flex;
    flex-direction: column;
    align-items: center;
}
.map-title {
    width: 1000px;
    text-align: left;
    margin-bottom: 15px;
    font-weight: bold;
    color: #343a40;
    font-size: 16px;
}
.map-container {
	position: relative; 
	width: 1000px;
	height: 550px;
	background-color: #ffffff;
	border: 1px solid #ced4da;
	background-image: url('images/floor4_map.png'); 
	background-size: 100% 100%;
	background-repeat: no-repeat;
    border-radius: 4px;
}
.room-btn {
	position: absolute;
	background-color: rgba(52, 58, 64, 0.05);
	border: 1px dashed #adb5bd;
	cursor: pointer;
	transition: all 0.2s;
	box-sizing: border-box;
    border-radius: 2px;
}
.room-btn:hover {
	background-color: rgba(33, 150, 243, 0.2);
	border: 2px solid #2196F3;
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
		<h2>Groupware</h2>
		<div class="nav-buttons">
			<% if ("Y".equals(loginEmp.getManager())) { %>
				<span style="color: #ffc107; font-weight: bold; font-size: 13px; margin-right: 5px;">[관리자]</span>
			<% } %>
			<span style="margin-right: 20px; font-size: 14px; color: #e9ecef;"><b><%=loginEmp.getEmpName()%></b>님</span>

			<% if ("Y".equals(loginEmp.getManager())) { %>
				<a href="adminEqList.do" class="nav-btn admin">재고 관리</a>
				<a href="admin.do" class="nav-btn admin">사원 관리</a>
                <span style="color: #495057;">|</span>
			<% } %>

			<a href="equipmentList.do" class="nav-btn">비품 대여 신청</a>
			<a href="documentList.do" class="nav-btn">기안 문서함</a>	
			<a href="myPage.do" class="nav-btn">마이페이지</a>
			<a href="logout.do" class="nav-btn logout">로그아웃</a>
		</div>
	</div>

    <div class="map-wrapper">
        <div class="map-title">■ 4층 회의실 예약 (도면에서 원하시는 회의실을 클릭하세요)</div>
        <div class="map-container">
            <div class="room-btn" id="room404" onclick="location.href='reserve.do?roomId=404'"></div>
            <div class="room-btn" id="room403" onclick="location.href='reserve.do?roomId=403'"></div>
            <div class="room-btn" id="room402" onclick="location.href='reserve.do?roomId=402'"></div>
            <div class="room-btn" id="room401" onclick="location.href='reserve.do?roomId=401'"></div>

            <div class="room-btn" id="roomInterview" onclick="location.href='reserve.do?roomId=Interview'"></div>
            <div class="room-btn" id="roomConsult" onclick="location.href='reserve.do?roomId=Consult'"></div>
            <div class="room-btn" id="roomMeeting" onclick="location.href='reserve.do?roomId=Meeting'"></div>
        </div>
    </div>

</body>
</html>