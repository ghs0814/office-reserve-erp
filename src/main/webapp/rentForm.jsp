<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.office.dto.EmployeeDTO" %>
<%@ page import="com.office.dto.EquipmentDTO" %>
<%
    // 1. 세션과 request 영역에서 로그인 정보 및 선택한 비품 정보를 가져옵니다.
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    EquipmentDTO eqInfo = (EquipmentDTO) request.getAttribute("equipment");
    
    // 비정상적인 접근(로그인 누락 등) 시 메인으로 돌려보냅니다.
    if (loginEmp == null || eqInfo == null) {
        response.sendRedirect("main.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 비품 대여 신청</title>
<style>
    /* 폼 레이아웃 및 디자인 설정 */
    body {
        font-family: 'Malgun Gothic', sans-serif;
        background-color: #f0f2f5;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
    }
    .form-container {
        background-color: white;
        padding: 40px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        width: 400px;
    }
    .form-container h2 {
        text-align: center;
        color: #333;
        margin-bottom: 20px;
    }
    /* 상단 비품 정보 요약 박스 스타일 */
    .info-box {
        background-color: #fff3e0;
        padding: 15px;
        border-radius: 8px;
        text-align: center;
        margin-bottom: 20px;
        border: 1px solid #ffe0b2;
    }
    .form-group { margin-bottom: 20px; }
    .form-group label {
        display: block;
        margin-bottom: 8px;
        font-weight: bold;
        color: #555;
    }
    .form-group input[type="date"] {
        width: 100%;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 4px;
        box-sizing: border-box;
    }
    .btn-group {
        display: flex;
        gap: 10px;
        margin-top: 30px;
    }
    .btn-submit {
        flex: 1;
        padding: 12px;
        background-color: #FF9800;
        color: white;
        border: none;
        border-radius: 4px;
        font-weight: bold;
        cursor: pointer;
    }
    .btn-cancel {
        flex: 1;
        padding: 12px;
        background-color: #ccc;
        color: #333;
        border: none;
        border-radius: 4px;
        font-weight: bold;
        cursor: pointer;
        text-align: center;
        text-decoration: none;
        box-sizing: border-box;
    }
</style>
<script>
    /**
     * 대여 기간 유효성 검사 함수
     * 반납일이 대여일보다 과거일 경우 제출을 중단합니다.
     */
    function validateRentForm() {
        const rentalDate = document.getElementById("rentalDate").value;
        const returnDate = document.getElementById("returnDate").value;
        
        if(rentalDate > returnDate) {
            alert("반납일은 대여일보다 빠를 수 없습니다.");
            return false;
        }
        return true;
    }
</script>
</head>
<body>

<div class="form-container">
    <h2>비품 대여 신청서</h2>
    
    <!-- 2. 신청 대상 비품의 이름과 신청자 성명을 화면에 표시 -->
    <div class="info-box">
        <h3 style="margin:0 0 10px 0; color:#e65100;"><%= eqInfo.getEqName() %></h3>
        <p style="margin:0; font-size:14px; color:#555;">신청자: <%= loginEmp.getEmpName() %></p>
    </div>

    <!-- 3. 대여 처리를 위한 데이터 전송 폼 (RentProcessController로 연결) -->
    <form action="rentProcess.do" method="post" onsubmit="return validateRentForm();">
        <!-- 서버 처리를 위해 비품번호와 사원번호를 숨김 필드로 전송 -->
        <input type="hidden" name="eqNo" value="<%= eqInfo.getEqNo() %>">
        <input type="hidden" name="empNo" value="<%= loginEmp.getEmpNo() %>">
        
        <div class="form-group">
            <label>대여 시작일</label>
            <input type="date" id="rentalDate" name="rentalDate" required>
        </div>
        
        <div class="form-group">
            <label>반납 예정일</label>
            <input type="date" id="returnDate" name="returnDate" required>
        </div>
        
        <div class="btn-group">
            <a href="equipmentList.do" class="btn-cancel">취소</a>
            <button type="submit" class="btn-submit">결재 올리기</button>
        </div>
    </form>
</div>

</body>
</html>