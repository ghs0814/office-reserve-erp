<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.office.dto.RentalHistoryDTO"%>
<%
RentalHistoryDTO doc = (RentalHistoryDTO) request.getAttribute("docDetail");
//데이터가 null일 경우 500 에러가 나지 않도록 방어하는 로직
if (doc == null) {
	out.println("<script>alert('존재하지 않거나 불러올 수 없는 기안입니다.'); history.back();</script>");
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>기안 상세 내용 (원본)</title>
<style>
body {
	font-family: 'Malgun Gothic', sans-serif;
	background-color: #f4f6f9;
	padding: 30px;
}

.panel {
	background: white;
	border: 1px solid #ddd;
	border-radius: 8px;
	padding: 30px;
	max-width: 600px;
	margin: 0 auto;
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
}

.info-row {
	margin-bottom: 15px;
	font-size: 16px;
}

.info-label {
	font-weight: bold;
	display: inline-block;
	width: 130px;
	color: #555;
}

.btn-back {
	display: block;
	width: 100%;
	padding: 12px;
	margin-top: 30px;
	text-align: center;
	background: #607D8B;
	color: white;
	text-decoration: none;
	border-radius: 4px;
	font-weight: bold;
}
</style>
</head>
<body>
	<div class="panel">
		<h2
			style="border-bottom: 2px solid #333; padding-bottom: 15px; margin-top: 0;">대여
			신청서 원본</h2>
		<div class="info-row">
			<span class="info-label">문서 번호:</span>
			<%=doc.getRentalNo()%></div>
		<div class="info-row">
			<span class="info-label">기안 제목:</span>
			<%=doc.getTitle() != null ? doc.getTitle() : "제목 없음"%></div>
		<div class="info-row">
			<span class="info-label">기안자:</span>
			<%=doc.getEmpName()%>
			(<%=doc.getEmpLevel()%>단계)
		</div>
		<div class="info-row">
			<span class="info-label">신청 비품 번호:</span>
			<%=doc.getEqNo()%></div>
		<div class="info-row">
			<span class="info-label">대여 신청일:</span>
			<%=doc.getRentalDate()%></div>
		<div class="info-row">
			<span class="info-label">반납 예정일:</span>
			<%=doc.getReturnDate()%></div>

		<a href="documentList.do" class="btn-back">기안 문서함으로 돌아가기</a>
	</div>
</body>
</html>