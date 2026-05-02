<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.office.dto.EmployeeDTO"%>
<%@ page import="com.office.dto.EquipmentDTO"%>
<%
EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
EquipmentDTO eqInfo = (EquipmentDTO) request.getAttribute("equipment");

if (loginEmp == null || eqInfo == null) {
	response.sendRedirect("main.jsp");
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사내 시스템 - 비품 대여 신청서</title>
<style>
    body { font-family: 'Segoe UI', 'Malgun Gothic', sans-serif; background-color: #f4f6f9; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
    .form-container { background-color: #ffffff; padding: 40px; border-radius: 8px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05); width: 420px; border-top: 5px solid #343a40; }
    .form-container h2 { text-align: left; color: #212529; margin-top: 0; margin-bottom: 20px; font-size: 20px; border-bottom: 2px solid #e9ecef; padding-bottom: 15px; }
    
    .info-box { background-color: #f8f9fa; padding: 15px; border-radius: 4px; margin-bottom: 25px; border: 1px solid #dee2e6; display: flex; justify-content: space-between; align-items: center; }
    .info-box .title { margin: 0; color: #343a40; font-size: 16px; font-weight: bold; }
    .info-box .stock { color: #495057; font-size: 13px; }
    .info-box .stock b { color: #212529; }

    .form-group { margin-bottom: 20px; }
    .form-group label { display: block; margin-bottom: 8px; font-weight: 600; color: #495057; font-size: 13px; }
    .form-group input { width: 100%; padding: 12px; border: 1px solid #ced4da; border-radius: 4px; box-sizing: border-box; background-color: #ffffff; font-size: 14px; }
    .form-group input:focus { border-color: #495057; outline: none; }
    
    .btn-group { display: flex; gap: 10px; margin-top: 30px; }
    .btn-submit { flex: 1; padding: 14px; background-color: #212529; color: white; border: none; border-radius: 4px; font-weight: bold; cursor: pointer; font-size: 14px; transition: 0.2s; }
    .btn-submit:hover { background-color: #000000; }
    .btn-cancel { flex: 1; padding: 14px; background-color: #ffffff; color: #495057; border: 1px solid #ced4da; border-radius: 4px; font-weight: bold; cursor: pointer; text-align: center; text-decoration: none; box-sizing: border-box; font-size: 14px; transition: 0.2s; }
    .btn-cancel:hover { background-color: #f8f9fa; }
</style>
<script>
	function validateRentForm() {
		const rentalDate = document.getElementById("rentalDate").value;
		const returnDate = document.getElementById("returnDate").value;
		if (rentalDate > returnDate) {
			alert("반납 예정일은 대여 시작일보다 빠를 수 없습니다.");
			return false;
		}
		return true;
	}
</script>
</head>
<body>

	<div class="form-container">
		<h2>전자 결재 기안 작성 (비품 대여)</h2>

		<div class="info-box">
            <div>
			    <p class="title"><%=eqInfo.getEqName()%></p>
                <p style="margin: 5px 0 0 0; font-size: 12px; color: #6c757d;">기안자: <%=loginEmp.getEmpName()%></p>
            </div>
            <div class="stock">대여 가능 <b><%=eqInfo.getRemainCount()%></b> EA</div>
		</div>

		<form action="rentProcess.do" method="post" onsubmit="return validateRentForm();">
			<input type="hidden" name="eqNo" value="<%=eqInfo.getEqNo()%>">
			<input type="hidden" name="empNo" value="<%=loginEmp.getEmpNo()%>">

			<div class="form-group">
				<label>대여 시작일</label> 
                <input type="date" id="rentalDate" name="rentalDate" required>
			</div>

			<div class="form-group">
				<label>반납 예정일</label> 
                <input type="date" id="returnDate" name="returnDate" required>
			</div>
			
			<div class="form-group">
				<label>기안 제목</label> 
                <input type="text" name="title" placeholder="결재선에 표시될 제목을 입력하세요" required>
			</div>
            
            <div class="form-group">
				<label>대여 수량</label> 
                <input type="number" name="reqCount" value="1" min="1" max="<%=eqInfo.getRemainCount()%>" required>
			</div>

			<div class="btn-group">
				<a href="equipmentList.do" class="btn-cancel">취소</a>
				<button type="submit" class="btn-submit">결재 상신</button>
			</div>
		</form>
	</div>

</body>
</html>