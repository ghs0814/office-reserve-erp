<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 회원가입</title>
<style>
body {
	font-family: 'Malgun Gothic', sans-serif;
	background-color: #f0f2f5;
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
	margin: 0;
}

.join-container {
	background-color: white;
	padding: 40px;
	border-radius: 8px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
	width: 350px;
}

.join-container h2 {
	text-align: center;
	color: #333;
	margin-bottom: 30px;
}

.form-group {
	margin-bottom: 20px;
}

.form-group label {
	display: block;
	margin-bottom: 5px;
	font-weight: bold;
	color: #555;
}

.form-group input {
	width: 100%;
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
}

.btn-join {
	width: 100%;
	padding: 12px;
	background-color: #4CAF50;
	color: white;
	border: none;
	border-radius: 4px;
	font-weight: bold;
	cursor: pointer;
	font-size: 16px;
}

.btn-join:hover {
	background-color: #45a049;
}

.btn-back {
	width: 100%;
	padding: 12px;
	background-color: #9e9e9e;
	color: white;
	border: none;
	border-radius: 4px;
	font-weight: bold;
	cursor: pointer;
	font-size: 16px;
	margin-top: 10px;
}
</style>
</head>
<body>

	<div class="join-container">
		<h2>회원가입</h2>
		<form action="joinProcess.do" method="post">
			<div class="form-group">
				<label for="empNo">사번 (숫자)</label> <input type="number" id="empNo"
					name="empNo" required placeholder="예: 2026001">
			</div>
			<div class="form-group">
				<label for="loginId">아이디</label> <input type="text" id="loginId"
					name="loginId" required placeholder="아이디 입력">
			</div>
			<div class="form-group">
				<label for="loginPw">비밀번호</label> <input type="password"
					id="loginPw" name="loginPw" required placeholder="비밀번호 입력">
			</div>
			<div class="form-group">
				<label for="empName">이름</label> <input type="text" id="empName"
					name="empName" required placeholder="예: 홍길동">
			</div>
			<div class="form-group">
				<label for="empLevel">직급 (권한 레벨)</label> <select id="empLevel"
					name="empLevel" required
					style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;">
					<option value="1">1단계 (일반 사원)</option>
					<option value="2">2단계 (팀장급)</option>
					<option value="3">3단계 (부장급)</option>
					<option value="4">4단계 (상무급)</option>
					<option value="5">5단계 (최고 관리자)</option>
				</select>
			</div>

			<button type="submit" class="btn-join">가입하기</button>
			<button type="button" class="btn-back"
				onclick="location.href='index.jsp'">취소</button>
		</form>
	</div>

</body>
</html>