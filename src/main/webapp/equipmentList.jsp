<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.office.dto.EquipmentDTO" %>
<%
    // Controller(EquipmentListController)로부터 전달받은 비품 목록 리스트
    List<EquipmentDTO> eqList = (List<EquipmentDTO>) request.getAttribute("eqList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 사내 비품 대여</title>
<style>
    /* 사용자 비품 목록 화면 스타일 */
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
    /* 카드 레이아웃을 위한 그리드 설정 */
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
    /* 재고 소진 시 버튼 스타일 */
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

    <!-- 비품 카드 리스트 출력 영역 -->
    <div class="grid-container">
        <%
            // 비품 리스트가 존재할 경우 반복 출력
            if (eqList != null && !eqList.isEmpty()) {
                for (EquipmentDTO eq : eqList) {
        %>
            <div class="eq-card">
                <div class="eq-name"><%= eq.getEqName() %></div>
                <div class="eq-count">
                    잔여 수량: <b><%= eq.getRemainCount() %></b> / <%= eq.getTotalCount() %>개
                </div>
                
                <% 
                    // 재고가 1개라도 남아있으면 대여 신청 가능
                    if (eq.getRemainCount() > 0) { 
                %>
                    <!-- 클릭 시 비품 번호를 파라미터로 신청서 폼(RentFormController)으로 전달 -->
                    <button class="btn-rent" onclick="location.href='rentForm.do?eqNo=<%= eq.getEqNo() %>'">
                        대여 신청하기
                    </button>
                <% 
                    } else { 
                %>
                    <!-- 재고가 없으면 버튼을 비활성화하고 안내 메시지 표시 -->
                    <button class="btn-disabled" disabled>대여 불가 (재고 소진)</button>
                <%  } %>
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