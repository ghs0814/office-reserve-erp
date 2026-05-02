<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.office.dto.EmployeeDTO"%>
<%
// 1. 보안 체크: 세션에서 로그인 정보를 가져오고, 없으면 로그인 페이지로 튕겨냄
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
/* 메인 화면 스타일링 및 레이아웃 */
body {
	font-family: 'Malgun Gothic', sans-serif;
	background-color: #f0f2f5;
	margin: 0;
	padding: 20px;
}

/* 상단 헤더 및 메뉴 바 스타일 */
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

/* 도면 이미지 컨테이너 및 배경 설정 */
.map-container {
	position: relative; /* 자식 요소인 room-btn들의 absolute 위치 기준점 */
	width: 1000px;
	height: 550px;
	background-color: white;
	margin: 0 auto;
	border: 2px solid #ccc;
	background-image: url('images/floor4_map.png'); /* 4층 도면 이미지 */
	background-size: 100% 100%;
	background-repeat: no-repeat;
}

/* 도면 위 투명 버튼 스타일 (회의실 클릭용) */
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

/* 버튼 마우스 호버 시 강조 효과 */
.room-btn:hover {
	background-color: rgba(76, 175, 80, 0.4);
	border: 2px solid #4CAF50;
	color: white;
}

/* 도면 좌표에 따른 회의실 버튼별 위치 값 (백분율) */
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
			<%-- 관리자(manager='Y')인 경우 이름 앞에 붉은색 [관리자] 텍스트 출력 --%>
			<% if ("Y".equals(loginEmp.getManager())) { %>
				<span style="color: #d9534f; font-weight: bold;">[관리자]</span>
			<% } %>
			<b><%=loginEmp.getEmpName()%></b>님 환영합니다.

			<!-- 2. 모든 사용자(전 직급)에게 공통으로 보이는 메뉴 -->
			<a href="equipmentList.do" class="logout-btn"
				style="background-color: #FF9800; margin-right: 10px; margin-left: 15px;">비품 대여 신청</a>
			<a href="myRentalList.do" class="logout-btn"
				style="background-color: #00BCD4; margin-right: 10px;">내 비품 대여 내역</a>
			<a href="myReserveList.do" class="logout-btn"
				style="background-color: #2196F3; margin-right: 10px;">내 예약 조회</a>

			
			
			<a href="documentList.do" class="logout-btn"
			 style="background-color: #4CAF50; margin-right: 10px;">기안 문서함</a>	

			<!-- 4. 최고 관리자(manager='Y') 전용 사원 관리 페이지 버튼 -->
			<% if ("Y".equals(loginEmp.getManager())) { %>
				<a href="adminEqList.do" class="logout-btn"
				style="background-color: #8D6E63; margin-right: 10px;">재고 관리</a>
				<a href="admin.do" class="logout-btn"
					style="background-color: #333; margin-right: 10px;">사원 관리</a>
			<% } %>

			<a href="logout.do" class="logout-btn">로그아웃</a>
		</div>
	</div>

	<!-- 5. 회의실 안내도 클릭 영역: 클릭 시 해당 roomId를 파라미터로 예약 화면(reserve.do)으로 이동 -->
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