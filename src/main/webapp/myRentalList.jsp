<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.office.dto.RentalHistoryDTO"%>
<%
// Controller에서 전달받은 DTO 리스트
List<RentalHistoryDTO> myList = (List<RentalHistoryDTO>) request.getAttribute("myList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 내 비품 대여 내역</title>
<style>
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

.status-badge {
	padding: 4px 8px;
	border-radius: 4px;
	font-size: 12px;
	font-weight: bold;
	color: white;
}

.bg-warning {
	background-color: #FFC107;
	color: #333;
} /* 승인대기 */
.bg-success {
	background-color: #4CAF50;
} /* 대여중 */
.bg-secondary {
	background-color: #9e9e9e;
} /* 반납완료/반려 */
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
	function processReturn(rentalNo) {
		if (confirm("해당 비품을 반납하시겠습니까?")) {
			//alert(rentalNo + "번 비품이 반납 처리되었습니다. (테스트)");
			//실제 연결 시: 
			location.href = 'returnProcess.do?rentalNo=' + rentalNo;
		}

	}
</script>
</head>
<body>

	<div class="header">
		<h2>내 비품 대여 내역</h2>
		<div>
			<a href="main.jsp"
				style="text-decoration: none; color: #333; font-weight: bold;">[메인으로
				돌아가기]</a>
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
				if (myList != null && !myList.isEmpty()) {
					for (RentalHistoryDTO item : myList) { // Map 대신 DTO 객체 사용
						String status = item.getStatus();
						String badgeClass = "bg-secondary";

						if ("승인대기".equals(status))
					badgeClass = "bg-warning";
						else if ("대여중".equals(status))
					badgeClass = "bg-success";
				%>
				<tr>
					<td><%=item.getRentalNo()%></td>
					<td><b><%=item.getEqName()%></b></td>
					<!-- DTO에서 이름 출력 -->
					<td><%=item.getRentalDate()%> ~ <%=item.getReturnDate()%></td>
					<td><span class="status-badge <%=badgeClass%>"><%=status%></span></td>
					<td>
						<%
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
					<td colspan="5" style="padding: 30px; color: #888;">대여 내역이
						없습니다.</td>
				</tr>
				<%
				}
				%>
			</tbody>
		</table>
	</div>

</body>
</html>