<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.office.dto.EmployeeDTO"%>
<%
    // 로그인 체크
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
<title>비밀번호 변경</title>
<style>
    body { font-family: 'Malgun Gothic', sans-serif; background-color: #f0f2f5; padding: 50px; }
    .container { background-color: white; padding: 40px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); width: 400px; margin: 0 auto; }
    h2 { text-align: center; margin-top: 0; border-bottom: 2px solid #333; padding-bottom: 15px; }
    
    .form-group { margin-bottom: 20px; }
    .form-group label { display: block; font-weight: bold; margin-bottom: 8px; color: #333; }
    .form-group input { width: 100%; padding: 10px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px; }
    
    .btn-submit { width: 100%; padding: 12px; background-color: #673AB7; color: white; border: none; border-radius: 4px; font-weight: bold; font-size: 16px; cursor: pointer; margin-top: 10px; }
    .btn-submit:hover { background-color: #512da8; }
    .btn-cancel { display: block; width: 100%; text-align: center; margin-top: 15px; color: #666; text-decoration: none; font-weight: bold; }
</style>
<script>
    // 폼 제출 전 자바스크립트로 간단한 유효성 검사
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
    <h2>비밀번호 변경</h2>
    
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
        
        <button type="submit" class="btn-submit">변경하기</button>
        <a href="myPage.do" class="btn-cancel">취소하고 돌아가기</a>
    </form>
</div>

</body>
</html>