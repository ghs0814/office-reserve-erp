<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.office.dto.EquipmentDTO" %>
<%
    List<EquipmentDTO> eqList = (List<EquipmentDTO>) request.getAttribute("eqList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사내 시스템 - 비품 대여 신청</title>
<style>
    body { font-family: 'Segoe UI', 'Malgun Gothic', sans-serif; background-color: #f4f6f9; padding: 40px; margin: 0; }
    .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; border-bottom: 2px solid #343a40; padding-bottom: 15px; }
    .header h2 { margin: 0; color: #212529; font-size: 22px; }
    .btn-back { padding: 10px 18px; background-color: #ffffff; color: #343a40; text-decoration: none; border: 1px solid #ced4da; border-radius: 4px; font-weight: bold; font-size: 13px; transition: 0.2s; }
    .btn-back:hover { background-color: #e9ecef; }
    
    .grid-container { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 20px; }
    .eq-card { background-color: #ffffff; border-radius: 6px; padding: 25px 20px; border: 1px solid #dee2e6; text-align: center; transition: box-shadow 0.2s; }
    .eq-card:hover { box-shadow: 0 5px 15px rgba(0,0,0,0.05); }
    .eq-name { font-size: 18px; font-weight: 700; color: #212529; margin-bottom: 12px; }
    .eq-count { font-size: 13px; color: #6c757d; margin-bottom: 20px; background-color: #f8f9fa; padding: 8px; border-radius: 4px; display: inline-block; }
    .eq-count b { color: #343a40; font-size: 15px; }
    
    .btn-rent { width: 100%; padding: 12px; background-color: #343a40; color: white; border: none; border-radius: 4px; font-weight: bold; font-size: 14px; cursor: pointer; transition: 0.2s; }
    .btn-rent:hover { background-color: #212529; }
    .btn-disabled { width: 100%; padding: 12px; background-color: #e9ecef; color: #adb5bd; border: 1px solid #dee2e6; border-radius: 4px; font-weight: bold; font-size: 14px; cursor: not-allowed; }
</style>
</head>
<body>

    <div class="header">
        <h2>공용 비품 대여 현황</h2>
        <div><a href="main.jsp" class="btn-back">시스템 메인으로</a></div>
    </div>

    <div class="grid-container">
        <% if (eqList != null && !eqList.isEmpty()) {
            for (EquipmentDTO eq : eqList) {
        %>
            <div class="eq-card">
                <div class="eq-name"><%= eq.getEqName() %></div>
                <div class="eq-count">잔여 수량 <b><%= eq.getRemainCount() %></b> / <%= eq.getTotalCount() %>EA</div>
                
                <% if (eq.getRemainCount() > 0) { %>
                    <button class="btn-rent" onclick="location.href='rentForm.do?eqNo=<%= eq.getEqNo() %>'">대여 신청서 작성</button>
                <% } else { %>
                    <button class="btn-disabled" disabled>재고 소진</button>
                <% } %>
            </div>
        <% } } else { %>
            <div style="grid-column: 1 / -1; text-align: center; color: #6c757d; padding: 40px; background: white; border: 1px solid #dee2e6; border-radius: 6px;">등록된 비품 목록이 없습니다.</div>
        <% } %>
    </div>

</body>
</html>