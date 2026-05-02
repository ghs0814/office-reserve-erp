<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.office.dto.RentalHistoryDTO"%>
<%
RentalHistoryDTO doc = (RentalHistoryDTO) request.getAttribute("docDetail");
if (doc == null) {
	out.println("<script>alert('존재하지 않거나 불러올 수 없는 기안입니다.'); history.back();</script>");
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사내 시스템 - 기안 원본</title>
<style>
body { font-family: 'Segoe UI', 'Malgun Gothic', sans-serif; background-color: #f4f6f9; padding: 40px; display: flex; justify-content: center; align-items: center; min-height: 80vh; margin: 0; }
.panel { background: #ffffff; border: 1px solid #dee2e6; border-radius: 8px; padding: 40px; width: 100%; max-width: 500px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05); border-top: 5px solid #495057; }
.panel h2 { border-bottom: 2px solid #e9ecef; padding-bottom: 15px; margin-top: 0; margin-bottom: 25px; color: #212529; font-size: 20px; }
.info-row { margin-bottom: 16px; font-size: 14px; color: #212529; display: flex; }
.info-label { font-weight: 600; width: 130px; color: #6c757d; flex-shrink: 0; }
.info-content { font-weight: 500; }
.btn-back { display: block; width: 100%; padding: 14px; margin-top: 35px; text-align: center; background: #f8f9fa; color: #495057; border: 1px solid #ced4da; text-decoration: none; border-radius: 4px; font-weight: bold; font-size: 14px; transition: 0.2s; box-sizing: border-box; }
.btn-back:hover { background: #e9ecef; }
</style>
</head>
<body>
	<div class="panel">
		<h2>기안 원본 상세</h2>
		<div class="info-row"><span class="info-label">문서 번호</span><span class="info-content"><%=doc.getRentalNo()%></span></div>
		<div class="info-row"><span class="info-label">기안 제목</span><span class="info-content"><%=doc.getTitle() != null ? doc.getTitle() : "제목 없음"%></span></div>
		<div class="info-row"><span class="info-label">기안자</span><span class="info-content"><%=doc.getEmpName()%> (<%=doc.getEmpLevel()%>단계)</span></div>
		<div class="info-row"><span class="info-label">신청 비품 번호</span><span class="info-content"><%=doc.getEqNo()%></span></div>
		<div class="info-row"><span class="info-label">대여 일자</span><span class="info-content"><%=doc.getRentalDate()%></span></div>
		<div class="info-row"><span class="info-label">반납 예정일</span><span class="info-content"><%=doc.getReturnDate()%></span></div>

		<a href="documentList.do" class="btn-back">기안 문서함으로 복귀</a>
	</div>
</body>
</html>