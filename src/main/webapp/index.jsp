<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 로그인</title>
<style>
    body {
        font-family: 'Segoe UI', 'Malgun Gothic', sans-serif;
        background-color: #e9ecef; /* 차분한 밝은 회색 */
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
    }
    .login-container {
        background-color: #ffffff;
        padding: 50px 40px;
        border-radius: 8px;
        box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
        width: 350px;
        border-top: 5px solid #343a40; /* 상단 진한 회색 포인트 */
    }
    .login-container h2 {
        margin-top: 0;
        margin-bottom: 30px;
        color: #212529;
        font-size: 22px;
        font-weight: 700;
        text-align: center;
        letter-spacing: -0.5px;
    }
    .input-group {
        margin-bottom: 20px;
    }
    .input-group input {
        width: 100%;
        padding: 14px;
        border: 1px solid #ced4da;
        border-radius: 4px;
        box-sizing: border-box;
        font-size: 14px;
        background-color: #f8f9fa;
        transition: border-color 0.2s;
    }
    .input-group input:focus {
        border-color: #495057;
        outline: none;
        background-color: #ffffff;
    }
    .btn-login {
        width: 100%;
        padding: 14px;
        background-color: #343a40; /* 다크 그레이 */
        color: #ffffff;
        border: none;
        border-radius: 4px;
        font-size: 15px;
        font-weight: bold;
        cursor: pointer;
        margin-top: 10px;
        transition: background-color 0.2s;
    }
    .btn-login:hover {
        background-color: #212529;
    }
    .btn-join {
        width: 100%;
        padding: 14px;
        background-color: #ffffff;
        color: #495057;
        border: 1px solid #ced4da;
        border-radius: 4px;
        font-size: 15px;
        font-weight: bold;
        cursor: pointer;
        margin-top: 10px;
        transition: all 0.2s;
    }
    .btn-join:hover {
        background-color: #f8f9fa;
        color: #212529;
    }
    .footer-links {
        text-align: center;
        margin-top: 25px;
    }
    .footer-links a {
        color: #6c757d;
        text-decoration: none;
        font-size: 13px;
        transition: color 0.2s;
    }
    .footer-links a:hover {
        color: #343a40;
        text-decoration: underline;
    }
</style>
</head>
<body>

    <div class="login-container">
        <h2>Groupware</h2>

        <form action="login.do" method="post">
            <div class="input-group">
                <input type="text" name="loginId" placeholder="사원번호를 입력하세요" required autofocus>
            </div>
            <div class="input-group">
                <input type="password" name="loginPw" placeholder="비밀번호를 입력하세요" required>
            </div>

            <button type="submit" class="btn-login">로그인</button>
            <button type="button" class="btn-join" onclick="location.href='join.jsp'">계정 등록 (회원가입)</button>

            <div class="footer-links">
                <a href="findPw.jsp">비밀번호를 잊으셨나요?</a>
            </div>
        </form>
    </div>

</body>
</html>