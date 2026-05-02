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
<title>사내 시스템 - 비밀번호 변경</title>
<style>
    body {
        font-family: 'Segoe UI', 'Malgun Gothic', sans-serif;
        background-color: #e9ecef;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
    }
    .container {
        background-color: #ffffff;
        padding: 40px;
        border-radius: 8px;
        box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
        width: 400px;
        border-top: 5px solid #343a40;
    }
    h2 {
        margin-top: 0;
        color: #212529;
        font-size: 20px;
        font-weight: 700;
        text-align: center;
        margin-bottom: 30px;
    }
    .form-group {
        margin-bottom: 20px;
    }
    .form-group label {
        display: block;
        font-size: 13px;
        font-weight: 600;
        margin-bottom: 8px;
        color: #495057;
    }
    .form-group input {
        width: 100%;
        padding: 12px;
        box-sizing: border-box;
        border: 1px solid #ced4da;
        border-radius: 4px;
        background-color: #f8f9fa;
        transition: border-color 0.2s;
    }
    .form-group input:focus {
        border-color: #495057;
        outline: none;
        background-color: #ffffff;
    }
    .btn-submit {
        width: 100%;
        padding: 14px;
        background-color: #343a40;
        color: #ffffff;
        border: none;
        border-radius: 4px;
        font-weight: bold;
        font-size: 15px;
        cursor: pointer;
        margin-top: 10px;
        transition: background-color 0.2s;
    }
    .btn-submit:hover { background-color: #212529; }
    .btn-cancel {
        display: block;
        width: 100%;
        text-align: center;
        margin-top: 20px;
        color: #6c757d;
        text-decoration: none;
        font-size: 13px;
        font-weight: bold;
    }
    .btn-cancel:hover { color: #343a40; text-decoration: underline; }
</style>
<script>
    function validateForm() {
        var currentPw = document.getElementById("currentPw").value;
        var newPw = document.getElementById("newPw").value;
        var confirmPw = document.getElementById("confirmPw").value;

        if (currentPw === "") {
            alert("현재 비밀번호를 입력해주세요.");
            return false;
        }
        if (newPw === "") {
            alert("새 비밀번호를 입력해주세요.");
            return false;
        }
        if (newPw !== confirmPw) {
            alert("새 비밀번호가 서로 일치하지 않습니다.");
            return false;
        }
        return true;
    }
</script>
</head>
<body>

<div class="container">
    <h2>보안 인증 (비밀번호 변경)</h2>
    
    <form action="changePwProcess.do" method="post" onsubmit="return validateForm();">
        <div class="form-group">
            <label for="currentPw">현재 비밀번호</label>
            <input type="password" id="currentPw" name="currentPw" required>
        </div>
        
        <div class="form-group">
            <label for="newPw">새 비밀번호</label>
            <input type="password" id="newPw" name="newPw" required>
        </div>
        
        <div class="form-group">
            <label for="confirmPw">새 비밀번호 확인</label>
            <input type="password" id="confirmPw" name="confirmPw" required>
        </div>
        
        <button type="submit" class="btn-submit">변경 사항 저장</button>
        <a href="myPage.do" class="btn-cancel">취소하고 마이페이지로 돌아가기</a>
    </form>
</div>

</body>
</html>