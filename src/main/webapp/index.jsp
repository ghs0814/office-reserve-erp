<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 로그인</title>
<style>
    /* 전체 배경과 중앙 정렬 설정 */
    body {
        font-family: 'Malgun Gothic', sans-serif;
        background-color: #f0f2f5; /* 눈이 편안한 밝은 회색 계열 */
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
    }
    
    /* 로그인 박스 컨테이너 */
    .login-container {
        background-color: white;
        padding: 40px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        width: 320px;
        text-align: center;
    }
    
    .login-container h2 {
        margin-bottom: 30px;
        color: #333;
        font-size: 24px;
    }
    
    /* 입력 필드 (아이디, 비밀번호) */
    .input-group {
        margin-bottom: 15px;
        text-align: left;
    }
    
    .input-group input {
        width: 100%;
        padding: 12px;
        border: 1px solid #ccc;
        border-radius: 4px;
        box-sizing: border-box; /* 패딩이 너비를 초과하지 않도록 설정 */
        font-size: 14px;
    }
    
    /* 포커스 시 입력 필드 강조 */
    .input-group input:focus {
        border-color: #4CAF50;
        outline: none;
        box-shadow: 0 0 5px rgba(76, 175, 80, 0.2);
    }
    
    /* 로그인 버튼 */
    .btn-login {
        width: 100%;
        padding: 12px;
        background-color: #4CAF50;
        color: white;
        border: none;
        border-radius: 4px;
        font-size: 16px;
        cursor: pointer;
        margin-top: 10px;
        transition: background-color 0.3s ease; /* 호버 시 부드러운 전환 효과 */
    }
    
    .btn-login:hover {
        background-color: #45a049;
    }
</style>
</head>
<body>

<div class="login-container">
    <h2>오피스 예약 시스템</h2>
    
    <!-- 
        form: 데이터를 묶어서 전송하는 영역입니다.
        action="login.do": 전송할 목적지 (서블릿 주소)입니다.
        method="post": 비밀번호 같은 민감한 정보를 숨겨서 전송합니다.
    -->
    <form action="login.do" method="post">
        
        <!-- 사번 입력란. name="loginId" 가 자바에서 데이터를 받을 때 쓰는 핵심 키워드입니다. -->
        <div class="input-group">
            <input type="text" name="loginId" placeholder="사번 (아이디)" required autofocus>
        </div>
        
        <!-- 비밀번호 입력란. name="loginPw" -->
        <div class="input-group">
            <input type="password" name="loginPw" placeholder="비밀번호" required>
        </div>
        
        <!-- 버튼을 누르면 form 안의 데이터가 action 주소로 넘어갑니다. -->
        <button type="submit" class="btn-login">로그인</button>
        
    </form>
</div>

</body>
</html>