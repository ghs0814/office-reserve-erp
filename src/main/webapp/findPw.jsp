<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 찾기</title>
<style>
    body { font-family: 'Malgun Gothic', sans-serif; background-color: #f0f2f5; padding: 50px; }
    .container { background-color: white; padding: 40px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); width: 400px; margin: 0 auto; }
    h2 { text-align: center; margin-top: 0; border-bottom: 2px solid #333; padding-bottom: 15px; }
    
    .form-group { margin-bottom: 20px; }
    .form-group label { display: block; font-weight: bold; margin-bottom: 8px; color: #333; }
    .form-group input { width: 100%; padding: 10px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px; }
    
    .btn-submit { width: 100%; padding: 12px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; font-weight: bold; font-size: 16px; cursor: pointer; margin-top: 10px; }
    .btn-submit:hover { background-color: #45a049; }
    .btn-cancel { display: block; width: 100%; text-align: center; margin-top: 15px; color: #666; text-decoration: none; font-weight: bold; }
</style>
</head>
<body>

<div class="container">
    <h2>비밀번호 찾기</h2>
    <p style="font-size: 13px; color: #666; text-align: center; margin-bottom: 25px;">
        가입 시 등록한 사번과 이름을 입력해 주세요.
    </p>
    
    <form action="findPwProcess.do" method="post">
        <div class="form-group">
            <label for="empNo">사원 번호 (사번)</label>
            <input type="number" id="empNo" name="empNo" required placeholder="예: 1001">
        </div>
        
        <div class="form-group">
            <label for="empName">이름</label>
            <input type="text" id="empName" name="empName" required placeholder="예: 홍길동">
        </div>
        
        <button type="submit" class="btn-submit">비밀번호 찾기</button>
        <a href="index.jsp" class="btn-cancel">로그인 화면으로 돌아가기</a>
    </form>
</div>

</body>
</html>