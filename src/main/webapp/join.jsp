<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 회원가입</title>
<style>
/* 회원가입 화면 레이아웃 및 디자인 설정 */
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
		<!-- 회원가입 폼 (예: join.jsp) -->
		<form action="joinProcess.do" method="post" id="joinForm">

			<!-- 사번 입력 및 조회 버튼 -->
			<div>
				<label>사번 (숫자)</label> <input type="text" id="empNo" name="empNo"
					placeholder="예: 2026001" required>
				<button type="button" id="checkEmpBtn">사번 조회</button>
			</div>

			<!-- 비밀번호 입력 -->
			<div>
				<label>비밀번호</label> <input type="password" id="empPw" name="empPw"
					placeholder="비밀번호 입력" required>
			</div>

			<!-- 이름 (readonly 속성으로 타이핑 금지) -->
			<div>
				<label>이름</label> <input type="text" id="empName" name="empName"
					placeholder="사번 조회 시 자동 입력" readonly>
			</div>

			<!-- 직급 (select 박스 대신, readonly 텍스트로 변경) -->
			<div>
				<label>직급 (권한 레벨)</label> <input type="text" id="empLevel"
					name="empLevel" placeholder="사번 조회 시 자동 입력" readonly>
			</div>

			<div>
				<button type="submit">가입하기</button>
				<button type="button">취소</button>
			</div>
		</form>
	</div>
	<script>
document.getElementById('checkEmpBtn').addEventListener('click', function() {
    var empNo = document.getElementById('empNo').value;

    if (empNo === '') {
        alert('사번을 입력해 주세요.');
        return;
    }

    // 서버의 getEmpInfo.jsp로 사번을 보내서 데이터 확인 (AJAX 통신)
    fetch('getEmpInfo.jsp?empNo=' + empNo)
        .then(function(response) {
            return response.json(); // 서버가 보낸 JSON 데이터를 변환
        })
        .then(function(data) {
            if (data.result === 'success') {
                // DB에 사번이 존재하면 화면의 빈칸을 채웁니다.
                document.getElementById('empName').value = data.empName;
                
                // 직급(1~5) 숫자에 맞춰 텍스트를 만들어 줍니다.
                var levelText = data.empLevel + "단계";
                if(data.empLevel === 5) levelText += " (최고관리자)";
                else if(data.empLevel === 1) levelText += " (일반 사원)";
                else levelText += " (중간 관리자)";

                document.getElementById('empLevel').value = levelText;
                alert('사원 정보가 확인되었습니다. 비밀번호를 설정하고 가입을 완료해 주세요.');
                
            } else {
                // DB에 없는 사번을 입력했을 경우
                document.getElementById('empName').value = '';
                document.getElementById('empLevel').value = '';
                alert('등록되지 않은 사번입니다. 인사팀에 문의해 주세요.');
            }
        })
        .catch(function(error) {
            console.error('Error:', error);
            alert('데이터베이스 조회 중 오류가 발생했습니다.');
        });
});
</script>
</body>
</html>