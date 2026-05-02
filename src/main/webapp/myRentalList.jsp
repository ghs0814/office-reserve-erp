<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.office.dto.RentalHistoryDTO"%>
<%
    List<RentalHistoryDTO> myList = (List<RentalHistoryDTO>) request.getAttribute("myList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사내 시스템 - 내 비품 대여 내역</title>
<style>
    body { font-family: 'Segoe UI', 'Malgun Gothic', sans-serif; background-color: #f4f6f9; padding: 40px; margin: 0; }
    .container { max-width: 1000px; margin: 0 auto; }

    .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; border-bottom: 2px solid #343a40; padding-bottom: 15px; }
    .page-header h2 { margin: 0; color: #212529; font-size: 22px; }
    
    .btn-back { padding: 10px 18px; background-color: #ffffff; color: #343a40; text-decoration: none; border: 1px solid #ced4da; border-radius: 4px; font-weight: bold; font-size: 13px; transition: 0.2s; }
    .btn-back:hover { background-color: #e9ecef; }

    .table-wrapper { background-color: #ffffff; border-radius: 6px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); overflow: hidden; border: 1px solid #e9ecef; }
    table { width: 100%; border-collapse: collapse; text-align: center; font-size: 14px; }
    th, td { padding: 14px 12px; border-bottom: 1px solid #e9ecef; }
    th { background-color: #f8f9fa; color: #495057; font-weight: 600; border-bottom: 2px solid #dee2e6; }
    tr:last-child td { border-bottom: none; }
    tr:hover { background-color: #f8f9fa; }

    .status-badge { padding: 5px 10px; border-radius: 4px; font-size: 12px; font-weight: bold; display: inline-block; }
    .bg-warning { background-color: #fff8e1; color: #f57c00; border: 1px solid #ffe0b2; }
    .bg-success { background-color: #e8f5e9; color: #2e7d32; border: 1px solid #c8e6c9; }
    .bg-secondary { background-color: #f1f3f5; color: #495057; border: 1px solid #dee2e6; }
    .bg-danger { background-color: #ffebee; color: #c62828; border: 1px solid #ffcdd2; }

    .btn-return { background-color: #343a40; color: #ffffff; border: 1px solid #343a40; padding: 6px 14px; border-radius: 4px; cursor: pointer; font-weight: bold; font-size: 12px; transition: 0.2s; }
    .btn-return:hover { background-color: #212529; }
</style>
<script>
	function processReturn(rentalNo) {
		if (confirm("해당 비품을 반납 처리하시겠습니까?")) {
			location.href = 'returnProcess.do?rentalNo=' + rentalNo;
		}
	}
</script>
</head>
<body>

<div class="container">
	<div class="page-header">
		<h2>내 비품 대여 전체 내역</h2>
        <a href="main.jsp" class="btn-back">시스템 메인으로</a>
	</div>

	<div class="table-wrapper">
		<table>
			<thead>
				<tr>
					<th>대여 번호</th><th>비품명</th><th>대여 기간</th><th>상태</th><th>비고 (액션)</th>
				</tr>
			</thead>
			<tbody>
				<% if (myList != null && !myList.isEmpty()) {
					for (RentalHistoryDTO item : myList) {
						String status = item.getStatus();
						String badgeClass = "bg-secondary"; 

						if ("승인대기".equals(status)) badgeClass = "bg-warning";
						else if ("대여중".equals(status)) badgeClass = "bg-success";
                        else if ("반려됨".equals(status)) badgeClass = "bg-danger";
				%>
				<tr>
					<td style="color: #6c757d;"><%=item.getRentalNo()%></td>
					<td style="font-weight: 600; color: #343a40;"><%=item.getEqName()%></td>
					<td><%=item.getRentalDate()%> ~ <%=item.getReturnDate()%></td>
					<td><span class="status-badge <%=badgeClass%>"><%=status%></span></td>
					<td>
						<% if ("대여중".equals(status)) { %>
						    <button class="btn-return" onclick="processReturn('<%=item.getRentalNo()%>')">반납 처리</button>
						<% } else { %> <span style="color: #ced4da;">-</span> <% } %>
					</td>
				</tr>
				<% } } else { %>
				    <tr><td colspan="5" style="padding: 40px; color: #6c757d;">대여 내역이 없습니다.</td></tr>
				<% } %>
			</tbody>
		</table>
	</div>
</div>

</body>
</html>