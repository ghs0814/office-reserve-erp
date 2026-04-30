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
<title>오피스 예약 시스템 - 비품 재고 관리</title>
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
    .action-bar {
        text-align: right;
        margin-bottom: 15px;
    }
    .table-container {
        background-color: white;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 2px 5px rgba(0,0,0,0.05);
        border: 1px solid #e0e0e0;
    }
    table {
        width: 100%;
        border-collapse: collapse;
        text-align: center;
    }
    th, td {
        padding: 12px;
        border-bottom: 1px solid #ddd;
    }
    th {
        background-color: #f5f5f5;
        color: #333;
        font-weight: bold;
    }
    .btn-add {
        background-color: #4CAF50;
        color: white;
        border: none;
        padding: 10px 20px;
        border-radius: 4px;
        cursor: pointer;
        font-weight: bold;
        text-decoration: none;
    }
    .btn-add:hover { background-color: #45a049; }
    
    .btn-edit {
        background-color: #FF9800;
        color: white;
        border: none;
        padding: 6px 12px;
        border-radius: 4px;
        cursor: pointer;
        font-weight: bold;
    }
    .text-danger { color: #f44336; font-weight: bold; }
</style>
<script>
    function alertReady() {
        alert("이 기능은 DB 연결 후 실제 구현될 예정입니다. (화면 테스트용)");
    }
</script>
</head>
<body>

    <div class="header">
        <h2>비품 재고 관리 (관리자용)</h2>
        <div>
            <a href="main.jsp" style="text-decoration: none; color: #333; font-weight: bold;">[메인으로 돌아가기]</a>
        </div>
    </div>

    <div class="action-bar">
        <a href="adminEqAddForm.do" class="btn-add">+ 신규 비품 등록</a>
    </div>

    <div class="table-container">
        <table>
            <thead>
                <tr>
                    <th>비품 번호</th>
                    <th>비품명</th>
                    <th>총 수량</th>
                    <th>잔여 수량</th>
                    <th>상태</th>
                    <th>관리</th>
                </tr>
            </thead>
            <tbody>
                <%
                    if (eqList != null && !eqList.isEmpty()) {
                        for (EquipmentDTO eq : eqList) {
                            boolean isOutOfStock = (eq.getRemainCount() == 0);
                %>
                <tr>
                    <td><%= eq.getEqNo() %></td>
                    <td><b><%= eq.getEqName() %></b></td>
                    <td><%= eq.getTotalCount() %>개</td>
                    <td class="<%= isOutOfStock ? "text-danger" : "" %>">
                        <%= eq.getRemainCount() %>개
                    </td>
                    <td>
                        <%= isOutOfStock ? "<span style='color:red;'>재고소진</span>" : "<span style='color:green;'>대여가능</span>" %>
                    </td>
                    <td>
                        <button class="btn-edit" onclick="alertReady()">수정/폐기</button>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="6" style="padding: 30px; color: #888;">등록된 비품이 없습니다.</td>
                </tr>
                <%  } %>
            </tbody>
        </table>
    </div>

</body>
</html>l>