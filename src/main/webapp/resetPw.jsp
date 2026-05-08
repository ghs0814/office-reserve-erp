<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // FindPwController에서 넘겨준 사번 받기
    String empNo = (String) request.getAttribute("empNo");
    
    // 비정상적인 접근(URL 직접 입력 등) 차단
    if (empNo == null) {
        out.println("<script>alert('정상적인 접근이 아닙니다.'); location.href='index.jsp';</script>");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사내 시스템 - 새 비밀번호 설정</title>
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
        padding: 50px 40px;
        border-radius: 8px;
        box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
        width: 350px;
        border-top: 5px solid #343a40;
    }
    h2 {
        margin-top: 0;
        color: #212529;
        font-size: 20px;
        font-weight: 700;
        text-align: center;
        margin-bottom: 25px;
    }
    .form-group { margin-bottom: 15px; }
    .form-group label { display: block; margin-bottom: 8px; font-size: 13px; color: #495057; font-weight: bold; }
    .form-group input { width: 100%; padding: 12px; border: 1px solid #ced4da; border-radius: 4px; box-sizing: border-box; font-size: 14px; }
    .form-group input:focus { border-color: #495057; outline: none; }
    .btn-submit { width: 100%; padding: 14px; background-color: #343a40; color: #ffffff; border: none; border-radius: 4px; font-weight: bold; font-size: 15px; cursor: pointer; margin-top: 15px; }
    .btn-submit:hover { background-color: #212529; }
</style>
<script>
    function validateForm() {
        var newPw = document.getElementById("newPw").value;
        var confirmPw = document.getElementById("confirmPw").value;

        if (newPw === "") {
            alert("새로운 비밀번호를 입력해주세요.");
            return false;
        }
        if (newPw !== confirmPw) {
            alert("입력하신 두 비밀번호가 서로 일치하지 않습니다.");
            return false;
        }
        return true;
    }
</script>
</head>
<body>

<div class="container">
    <h2>새 비밀번호 설정</h2>
    
    <form action="resetPwProcess.do" method="post" onsubmit="return validateForm();">
        <input type="hidden" name="empNo" value="<%=empNo%>">
        
        <div class="form-group">
            <label for="newPw">새로운 비밀번호</label>
            <input type="password" id="newPw" name="newPw" required autofocus>
        </div>
        
        <div class="form-group">
            <label for="confirmPw">새로운 비밀번호 확인</label>
            <input type="password" id="confirmPw" name="confirmPw" required>
        </div>
        
        <button type="submit" class="btn-submit">비밀번호 변경 완료</button>
    </form>
</div>

</body>
</html>