<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>계정 등록</title>
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
    .join-container {
        background-color: #ffffff;
        padding: 40px;
        border-radius: 8px;
        box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
        width: 380px;
        border-top: 5px solid #495057;
    }
    .join-container h2 {
        text-align: center;
        color: #212529;
        margin-top: 0;
        margin-bottom: 30px;
        font-size: 20px;
    }
    .form-group {
        margin-bottom: 20px;
    }
    .form-group label {
        display: block;
        margin-bottom: 8px;
        font-size: 13px;
        font-weight: 600;
        color: #495057;
    }
    .input-row {
        display: flex;
        gap: 10px;
    }
    .form-group input {
        flex: 1;
        width: 100%;
        padding: 12px;
        border: 1px solid #ced4da;
        border-radius: 4px;
        box-sizing: border-box;
        background-color: #f8f9fa;
    }
    .form-group input:focus {
        border-color: #495057;
        outline: none;
        background-color: #ffffff;
    }
    .form-group input[readonly] {
        background-color: #e9ecef;
        color: #6c757d;
        cursor: not-allowed;
    }
    .btn-check {
        padding: 0 15px;
        background-color: #6c757d;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-weight: bold;
        font-size: 13px;
    }
    .btn-check:hover { background-color: #5a6268; }
    
    .btn-group {
        display: flex;
        gap: 10px;
        margin-top: 30px;
    }
    .btn-submit, .btn-cancel {
        flex: 1;
        padding: 14px;
        border: none;
        border-radius: 4px;
        font-weight: bold;
        cursor: pointer;
        font-size: 14px;
    }
    .btn-submit {
        background-color: #343a40;
        color: white;
    }
    .btn-submit:hover { background-color: #212529; }
    .btn-cancel {
        background-color: #e9ecef;
        color: #495057;
        text-align: center;
        text-decoration: none;
        box-sizing: border-box;
    }
    .btn-cancel:hover { background-color: #dde2e6; }
</style>
</head>
<body>

    <div class="join-container">
        <h2>사내 시스템 계정 등록</h2>
        
        <form action="joinProcess.do" method="post" id="joinForm">
            <div class="form-group">
                <label>사원번호 확인</label>
                <div class="input-row">
                    <input type="text" id="empNo" name="empNo" placeholder="사번 입력 (숫자)" required>
                    <button type="button" class="btn-check" id="checkEmpBtn">정보 확인</button>
                </div>
            </div>

            <div class="form-group">
                <label>비밀번호 설정</label>
                <input type="password" id="empPw" name="empPw" placeholder="사용할 비밀번호 입력" required>
            </div>

            <div class="form-group">
                <label>성명</label>
                <input type="text" id="empName" name="empName" placeholder="사원번호 확인 시 자동 입력" readonly>
            </div>

            <div class="form-group">
                <label>부여된 권한 레벨</label>
                <input type="text" id="empLevel" name="empLevel" placeholder="사원번호 확인 시 자동 입력" readonly>
            </div>

            <div class="btn-group">
                <a href="index.jsp" class="btn-cancel">취소</a>
                <button type="submit" class="btn-submit">등록 완료</button>
            </div>
        </form>
    </div>

<script>
document.getElementById('checkEmpBtn').addEventListener('click', function() {
    var empNo = document.getElementById('empNo').value;
    if (empNo === '') {
        alert('사원번호를 입력해 주세요.');
        return;
    }

    fetch('getEmpInfo.jsp?empNo=' + empNo)
        .then(response => response.json())
        .then(data => {
            if (data.result === 'success') {
                document.getElementById('empName').value = data.empName;
                
                var levelText = data.empLevel + "단계";
                if(data.empLevel === 5) levelText += " (최고 관리자)";
                else if(data.empLevel === 1) levelText += " (일반 사원)";
                else levelText += " (중간 관리자)";

                document.getElementById('empLevel').value = levelText;
                alert('사원 정보가 확인되었습니다. 비밀번호를 설정해 주세요.');
            } else {
                document.getElementById('empName').value = '';
                document.getElementById('empLevel').value = '';
                alert('등록되지 않은 사원번호입니다. 인사팀에 문의해 주세요.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('정보 조회 중 시스템 오류가 발생했습니다.');
        });
});
</script>
</body>
</html>