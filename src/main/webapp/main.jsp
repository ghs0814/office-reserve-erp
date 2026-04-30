<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
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

#room404 {
	top: 4%;
	left: 4%;
	width: 24%;
	height: 34%;
}

#room403 {
	top: 4%;
	left: 29%;
	width: 24%;
	height: 34%;
}

#room402 {
	top: 4%;
	left: 53%;
	width: 19%;
	height: 34%;
}

#room401 {
	top: 4%;
	left: 73%;
	width: 20%;
	height: 34%;
}

#roomInterview {
	top: 56%;
	left: 38%;
	width: 13%;
	height: 33%;
}

#roomConsult {
	top: 56%;
	left: 51%;
	width: 15%;
	height: 33%;
}

#roomMeeting {
	top: 39%;
	left: 81%;
	width: 12%;
	height: 49%;
}
</style>
</head>
<body>

	<div class="header">
		<h2>오피스 예약 시스템</h2>
		<div>
			<b><%=loginEmp.getEmpName()%></b>님 환영합니다.

			<!-- 모든 사용자에게 보이는 공통 메뉴 -->
			<a href="equipmentList.do" class="logout-btn"
				style="background-color: #FF9800; margin-right: 10px;">비품 대여 신청</a>
			<a href="myRentalList.do" class="logout-btn"
				style="background-color: #00BCD4; margin-right: 10px;">내 비품 대여
				내역</a> <a href="myReserveList.do" class="logout-btn"
				style="background-color: #2196F3; margin-right: 10px;">내 예약 조회</a>

			<!-- 관리자에게만 보이는 메뉴 (직급이 '팀장'이거나 부서가 '관리부'인 경우 등) -->
			<!-- 테스트를 위해 이름이 '관리자' 이거나, 사번이 특정 번호일 때 열리도록 임시 설정 -->
			<%
			// 실제 DB 연동 시 loginEmp.getRole() == 1 등으로 처리
			if (loginEmp.getEmpLevel() >= 2) {
			%>
			<a href="managerApproval.do" class="logout-btn"
				style="background-color: #673AB7; margin-right: 10px;">관리자 결재함</a>
			<!-- 아래 한 줄을 새로 추가합니다 -->
			<a href="adminEqList.do" class="logout-btn"
				style="background-color: #8D6E63; margin-right: 10px;">재고 관리</a>
			<%
			}
			%>

			<a href="logout.do" class="logout-btn">로그아웃</a>
		</div>
	</div>

	<div class="map-container">
		<div class="room-btn" id="room404"
			onclick="location.href='reserve.do?roomId=404'"></div>
		<div class="room-btn" id="room403"
			onclick="location.href='reserve.do?roomId=403'"></div>
		<div class="room-btn" id="room402"
			onclick="location.href='reserve.do?roomId=402'"></div>
		<div class="room-btn" id="room401"
			onclick="location.href='reserve.do?roomId=401'"></div>

		<div class="room-btn" id="roomInterview"
			onclick="location.href='reserve.do?roomId=Interview'"></div>
		<div class="room-btn" id="roomConsult"
			onclick="location.href='reserve.do?roomId=Consult'"></div>
		<div class="room-btn" id="roomMeeting"
			onclick="location.href='reserve.do?roomId=Meeting'"></div>
	</div>

</body>
</html>