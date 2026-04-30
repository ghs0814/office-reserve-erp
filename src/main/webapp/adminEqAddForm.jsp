<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 신규 비품 등록</title>
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
    .form-container {
        background-color: white;
        padding: 40px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        width: 400px;
    }
    .form-container h2 {
        text-align: center;
        color: #333;
        margin-bottom: 30px;
    }
    .form-group { margin-bottom: 20px; }
    .form-group label {
        display: block;
        margin-bottom: 8px;
        font-weight: bold;
        color: #555;
    }
    .form-group input[type="text"],
    .form-group input[type="number"] {
        width: 100%;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 4px;
        box-sizing: border-box;
    }
    .btn-group {
        display: flex;
        gap: 10px;
        margin-top: 30px;
    }
    .btn-submit {
        flex: 1;
        padding: 12px;
        background-color: #4CAF50;
        color: white;
        border: none;
        border-radius: 4px;
        font-weight: bold;
        cursor: pointer;
    }
    .btn-cancel {
        flex: 1;
        padding: 12px;
        background-color: #ccc;
        color: #333;
        border: none;
        border-radius: 4px;
        font-weight: bold;
        cursor: pointer;
        text-align: center;
        text-decoration: none;
        box-sizing: border-box;
    }
</style>
</head>
<body>

<div class="form-container">
    <h2>신규 비품 등록</h2>
    
    <form action="adminEqAddProcess.do" method="post">
        <div class="form-group">
            <label>비품명</label>
            <input type="text" name="eqName" placeholder="예: 삼성 컬러 레이저 프린터" required>
        </div>
        
        <div class="form-group">
            <label>입고 수량</label>
            <input type="number" name="totalCount" min="1" placeholder="수량을 입력하세요" required>
        </div>
        
        <div class="btn-group">
            <a href="adminEqList.do" class="btn-cancel">취소</a>
            <button type="submit" class="btn-submit">등록하기</button>
        </div>
    </form>
</div>

</body>
</html>