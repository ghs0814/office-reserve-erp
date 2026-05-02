<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 찾기</title>
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
        border-top: 5px solid #6c757d;
    }
    h2 {
        margin-top: 0;
        color: #212529;
        font-size: 20px;
        font-weight: 700;
        text-align: center;
        margin-bottom: 10px;
    }
    p.desc {
        font-size: 13px;
        color: #6c757d;
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
    }
    .btn-cancel:hover { color: #343a40; text-decoration: underline; }
</style>
</head>
<body>

<div class="container">
    <h2>비밀번호 찾기</h2>
    <p class="desc">가입 시 등록한 사원번호와 이름을 입력해 주세요.</p>
    
    <form action="findPwProcess.do" method="post">
        <div class="form-group">
            <label for="empNo">사원번호</label>
            <input type="number" id="empNo" name="empNo" required placeholder="예: 2026001">
        </div>
        
        <div class="form-group">
            <label for="empName">이름</label>
            <input type="text" id="empName" name="empName" required placeholder="이름 입력">
        </div>
        
        <button type="submit" class="btn-submit">정보 확인</button>
        <a href="index.jsp" class="btn-cancel">로그인 화면으로 돌아가기</a>
    </form>
</div>

</body>
</html>