<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.office.dto.EquipmentDTO" %>
<%
    // Controller에서 전달받은 비품 목록
    List<EquipmentDTO> eqList = (List<EquipmentDTO>) request.getAttribute("eqList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 사내 비품 대여</title>
<style>
    body {
        font-family: 'Malgun Gothic', sans-serif;
        background-color: #f8f9fa;
        padding: 40px;
    }
    .header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 30px;
    }
    .grid-container {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
        gap: 20px;
    }
    .eq-card {
        background-color: white;
        border-radius: 8px;
        padding: 20px;
        box-shadow: 0 2px 5px rgba(0,0,0,0.05);
        border: 1px solid #e0e0e0;
        text-align: center;
    }
    .eq-name {
        font-size: 18px;
        font-weight: bold;
        color: #333;
        margin-bottom: 15px;
    }
    .eq-count {
        font-size: 14px;
        color: #666;
        margin-bottom: 20px;
    }
    .eq-count b {
        color: #2196F3;
        font-size: 16px;
    }
    .btn-rent {
        width: 100%;
        padding: 10px;
        background-color: #4CAF50;
        color: white;
        border: none;
        border-radius: 4px;
        font-weight: bold;
        cursor: pointer;
    }
    .btn-rent:hover { background-color: #45a049; }
    .btn-disabled {
        width: 100%;
        padding: 10px;
        background-color: #ccc;
        color: #666;
        border: none;
        border-radius: 4px;
        font-weight: bold;
        cursor: not-allowed;
    }
</style>
</head>
<body>

    <div class="header">
        <h2>사내 비품 대여 신청</h2>
        <div>
            <a href="main.jsp" style="text-decoration: none; color: #333; font-weight: bold;">[메인으로 돌아가기]</a>
        </div>
    </div>

    <div class="grid-container">
        <%
            if (eqList != null && !eqList.isEmpty()) {
                for (EquipmentDTO eq : eqList) {
        %>
            <div class="eq-card">
                <div class="eq-name"><%= eq.getEqName() %></div>
                <div class="eq-count">
                    잔여 수량: <b><%= eq.getRemainCount() %></b> / <%= eq.getTotalCount() %>개
                </div>
                
                <% if (eq.getRemainCount() > 0) { %>
                    <!-- 남은 수량이 있으면 대여 폼으로 이동 (eqNo 파라미터 전달) -->
                    <button class="btn-rent" onclick="location.href='rentForm.do?eqNo=<%= eq.getEqNo() %>'">
                        대여 신청하기
                    </button>
                <% } else { %>
                    <!-- 남은 수량이 0이면 버튼 비활성화 -->
                    <button class="btn-disabled" disabled>대여 불가 (재고 소진)</button>
                <% } %>
            </div>
        <%
                }
            } else {
        %>
            <div style="grid-column: 1 / -1; text-align: center; color: #888;">
                등록된 비품 목록이 없습니다.
            </div>
        <%  } %>
    </div>

</body>
</html>