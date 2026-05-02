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
	background-color: #212529; 
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
    padding: 30px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.05);
    border: 1px solid #e9ecef;
    display: flex;
    flex-direction: column;
    align-items: center;
}

/* ★ 신규 추가: 모던한 오피스 예약 타이틀 영역 */
.reservation-header {
    width: 1000px;
    text-align: left;
    margin-bottom: 25px;
    padding-bottom: 15px;
    border-bottom: 2px solid #212529;
}
.reservation-header h3 {
    margin: 0 0 5px 0;
    font-size: 24px;
    color: #212529;
    font-weight: 800;
    letter-spacing: -0.5px;
}
.reservation-header p {
    margin: 0;
    font-size: 14px;
    color: #6c757d;
}

/* 층수 선택 탭 디자인 */
.floor-tabs {
    display: flex;
    gap: 10px;
    margin-bottom: 20px;
    width: 1000px;
}
.floor-tab {
    padding: 10px 30px;
    background-color: #ffffff;
    border: 1px solid #ced4da;
    border-radius: 20px;
    font-size: 14px;
    font-weight: bold;
    color: #495057;
    cursor: pointer;
    transition: 0.2s;
}
.floor-tab:hover { background-color: #f8f9fa; border-color: #adb5bd; }
.floor-tab.active {
    background-color: #343a40;
    color: #ffffff;
    border-color: #343a40;
    box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.map-container {
	position: relative; 
	width: 1000px;
	height: 550px;
	background-color: #ffffff;
	border: 1px solid #ced4da;
	background-size: 100% 100%;
	background-repeat: no-repeat;
    border-radius: 4px;
}

/* 각 층별 배경 이미지 매핑 (기본적으로 1층만 보이게 설정) */
#map1 { background-image: url('images/floor1_map.png'); display: block; }
#map2 { background-image: url('images/floor2_map.png'); display: none; }
#map3 { background-image: url('images/floor3_map.png'); display: none; }
#map4 { background-image: url('images/floor4_map.png'); display: none; }
#map5 { background-image: url('images/floor5_map.png'); display: none; }

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

/* 1~5층 회의실 공통 좌표 적용 (코드를 효율적으로 그룹화) */
#room104, #room204, #room304, #room404, #room504 { top: 4%; left: 4%; width: 24%; height: 34%; }
#room103, #room203, #room303, #room403, #room503 { top: 4%; left: 29%; width: 24%; height: 34%; }
#room102, #room202, #room302, #room402, #room502 { top: 4%; left: 53%; width: 19%; height: 34%; }
#room101, #room201, #room301, #room401, #room501 { top: 4%; left: 73%; width: 20%; height: 34%; }

#roomInterview1, #roomInterview2, #roomInterview3, #roomInterview, #roomInterview5 { top: 56%; left: 38%; width: 13%; height: 33%; }
#roomConsult1, #roomConsult2, #roomConsult3, #roomConsult, #roomConsult5 { top: 56%; left: 51%; width: 15%; height: 33%; }
#roomMeeting1, #roomMeeting2, #roomMeeting3, #roomMeeting, #roomMeeting5 { top: 39%; left: 81%; width: 12%; height: 49%; }
</style>

<script>
    // 층수 탭 전환을 위한 JavaScript 함수 (1~5층 통합 처리)
    function switchFloor(floor) {
        // 모든 맵과 탭의 활성화 상태를 초기화
        for (let i = 1; i <= 5; i++) {
            document.getElementById('map' + i).style.display = 'none';
            document.getElementById('tab' + i).classList.remove('active');
        }
        
        // 클릭한 층수만 활성화
        document.getElementById('map' + floor).style.display = 'block';
        document.getElementById('tab' + floor).classList.add('active');
    }
</script>
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
        
        <!-- 오피스 예약 타이틀 영역 -->
        <div class="reservation-header">
            <h3>오피스 예약</h3>
            <p>예약을 진행하실 층수와 회의실을 도면에서 선택해 주세요.</p>
        </div>

        <!-- 층수 선택 탭 영역 -->
        <div class="floor-tabs">
            <button class="floor-tab active" id="tab1" onclick="switchFloor(1)">1층</button>
            <button class="floor-tab" id="tab2" onclick="switchFloor(2)">2층</button>
            <button class="floor-tab" id="tab3" onclick="switchFloor(3)">3층</button>
            <button class="floor-tab" id="tab4" onclick="switchFloor(4)">4층</button>
            <button class="floor-tab" id="tab5" onclick="switchFloor(5)">5층</button>
        </div>

        <!-- 1층 도면 영역 -->
        <div id="map1" class="map-container">
            <div class="room-btn" id="room104" onclick="location.href='reserve.do?roomId=104'"></div>
            <div class="room-btn" id="room103" onclick="location.href='reserve.do?roomId=103'"></div>
            <div class="room-btn" id="room102" onclick="location.href='reserve.do?roomId=102'"></div>
            <div class="room-btn" id="room101" onclick="location.href='reserve.do?roomId=101'"></div>
            <div class="room-btn" id="roomInterview1" onclick="location.href='reserve.do?roomId=Interview1'"></div>
            <div class="room-btn" id="roomConsult1" onclick="location.href='reserve.do?roomId=Consult1'"></div>
            <div class="room-btn" id="roomMeeting1" onclick="location.href='reserve.do?roomId=Meeting1'"></div>
        </div>

        <!-- 2층 도면 영역 -->
        <div id="map2" class="map-container">
            <div class="room-btn" id="room204" onclick="location.href='reserve.do?roomId=204'"></div>
            <div class="room-btn" id="room203" onclick="location.href='reserve.do?roomId=203'"></div>
            <div class="room-btn" id="room202" onclick="location.href='reserve.do?roomId=202'"></div>
            <div class="room-btn" id="room201" onclick="location.href='reserve.do?roomId=201'"></div>
            <div class="room-btn" id="roomInterview2" onclick="location.href='reserve.do?roomId=Interview2'"></div>
            <div class="room-btn" id="roomConsult2" onclick="location.href='reserve.do?roomId=Consult2'"></div>
            <div class="room-btn" id="roomMeeting2" onclick="location.href='reserve.do?roomId=Meeting2'"></div>
        </div>

        <!-- 3층 도면 영역 -->
        <div id="map3" class="map-container">
            <div class="room-btn" id="room304" onclick="location.href='reserve.do?roomId=304'"></div>
            <div class="room-btn" id="room303" onclick="location.href='reserve.do?roomId=303'"></div>
            <div class="room-btn" id="room302" onclick="location.href='reserve.do?roomId=302'"></div>
            <div class="room-btn" id="room301" onclick="location.href='reserve.do?roomId=301'"></div>
            <div class="room-btn" id="roomInterview3" onclick="location.href='reserve.do?roomId=Interview3'"></div>
            <div class="room-btn" id="roomConsult3" onclick="location.href='reserve.do?roomId=Consult3'"></div>
            <div class="room-btn" id="roomMeeting3" onclick="location.href='reserve.do?roomId=Meeting3'"></div>
        </div>

        <!-- 4층 도면 영역 -->
        <div id="map4" class="map-container">
            <div class="room-btn" id="room404" onclick="location.href='reserve.do?roomId=404'"></div>
            <div class="room-btn" id="room403" onclick="location.href='reserve.do?roomId=403'"></div>
            <div class="room-btn" id="room402" onclick="location.href='reserve.do?roomId=402'"></div>
            <div class="room-btn" id="room401" onclick="location.href='reserve.do?roomId=401'"></div>
            <div class="room-btn" id="roomInterview" onclick="location.href='reserve.do?roomId=Interview'"></div>
            <div class="room-btn" id="roomConsult" onclick="location.href='reserve.do?roomId=Consult'"></div>
            <div class="room-btn" id="roomMeeting" onclick="location.href='reserve.do?roomId=Meeting'"></div>
        </div>

        <!-- 5층 도면 영역 -->
        <div id="map5" class="map-container">
            <div class="room-btn" id="room504" onclick="location.href='reserve.do?roomId=504'"></div>
            <div class="room-btn" id="room503" onclick="location.href='reserve.do?roomId=503'"></div>
            <div class="room-btn" id="room502" onclick="location.href='reserve.do?roomId=502'"></div>
            <div class="room-btn" id="room501" onclick="location.href='reserve.do?roomId=501'"></div>
            <div class="room-btn" id="roomInterview5" onclick="location.href='reserve.do?roomId=Interview5'"></div>
            <div class="room-btn" id="roomConsult5" onclick="location.href='reserve.do?roomId=Consult5'"></div>
            <div class="room-btn" id="roomMeeting5" onclick="location.href='reserve.do?roomId=Meeting5'"></div>
        </div>

    </div>

</body>
</html>