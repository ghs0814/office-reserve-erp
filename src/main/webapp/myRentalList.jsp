<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.office.dto.RentalHistoryDTO"%>
<%
// 1. Controller로부터 전달받은 사용자의 개인 대여 내역 리스트입니다.
List<RentalHistoryDTO> myList = (List<RentalHistoryDTO>) request.getAttribute("myList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 내 비품 대여 내역</title>
<style>
/* 화면 레이아웃 및 상태별 배지 색상 스타일 설정 */
body {
	font-family: 'Malgun Gothic', sans-serif;
	background-color: #f8f9fa;
	padding: 40px;
}

.header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 30px;
}

.table-container {
	background-color: white;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
	border: 1px solid #e0e0e0;
}

table {
	width: 100%;
	border-collapse: collapse;
	text-align: center;
}

th, td {
	padding: 12px;
	border-bottom: 1px solid #ddd;
}

th {
	background-color: #f5f5f5;
	color: #333;
	font-weight: bold;
}

/* 결재 상태를 나타내는 배지 공통 스타일 */
.status-badge {
	padding: 4px 8px;
	border-radius: 4px;
	font-size: 12px;
	font-weight: bold;
	color: white;
}

/* 상태별 색상 구분 */
.bg-warning { background-color: #FFC107; color: #333; } /* 승인대기: 노란색 */
.bg-success { background-color: #4CAF50; }             /* 대여중: 초록색 */
.bg-secondary { background-color: #9e9e9e; }           /* 반납완료/반려: 회색 */

.btn-return {
	background-color: #2196F3;
	color: white;
	border: none;
	padding: 6px 12px;
	border-radius: 4px;
	cursor: pointer;
	font-weight: bold;
}

.btn-return:hover {
	background-color: #0b7dda;
}
</style>
<script>
    // 2. 비품 반납 확인 및 처리 함수
	function processReturn(rentalNo) {
		if (confirm("해당 비품을 반납하시겠습니까?")) {
			// 확인 클릭 시 반납 처리 컨트롤러로 대여번호를 전송하며 이동합니다.
			location.href = 'returnProcess.do?rentalNo=' + rentalNo;
		}
	}
</script>
</head>
<body>

	<div class="header">
		<h2>내 비품 대여 내역</h2>
		<div>
			<a href="main.jsp" style="text-decoration: none; color: #333; font-weight: bold;">[메인으로 돌아가기]</a>
		</div>
	</div>

	<div class="table-container">
		<table>
			<thead>
				<tr>
					<th>대여 번호</th>
					<th>비품명</th>
					<th>대여 기간</th>
					<th>상태</th>
					<th>비고 (액션)</th>
				</tr>
			</thead>
			<tbody>
				<%
				// 3. 내 대여 내역이 존재할 경우 리스트를 반복 출력합니다.
				if (myList != null && !myList.isEmpty()) {
					for (RentalHistoryDTO item : myList) {
						String status = item.getStatus();
						String badgeClass = "bg-secondary"; // 기본 색상(회색)

                        // 4. 현재 상태에 따라 배지 클래스를 동적으로 할당합니다.
						if ("승인대기".equals(status))
					        badgeClass = "bg-warning";
						else if ("대여중".equals(status))
					        badgeClass = "bg-success";
				%>
				<tr>
					<td><%=item.getRentalNo()%></td>
					<td><b><%=item.getEqName()%></b></td>
					<td><%=item.getRentalDate()%> ~ <%=item.getReturnDate()%></td>
					<td><span class="status-badge <%=badgeClass%>"><%=status%></span></td>
					<td>
						<%
						// 5. '대여중'인 상태일 때만 반납 버튼을 보여줍니다.
						if ("대여중".equals(status)) {
						%>
						<button class="btn-return"
							onclick="processReturn('<%=item.getRentalNo()%>')">반납하기</button>
						<%
						} else {
						%> - <%
						}
						%>
					</td>
				</tr>
				<%
				}
				} else {
				%>
				<tr>
					<td colspan="5" style="padding: 30px; color: #888;">대여 내역이 없습니다.</td>
				</tr>
				<%
				}
				%>
			</tbody>
		</table>
	</div>

</body>
</html>