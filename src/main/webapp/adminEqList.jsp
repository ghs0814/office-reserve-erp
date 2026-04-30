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
<title>오피스 예약 시스템 - 재고 관리</title>
<style>
    body { font-family: 'Malgun Gothic', sans-serif; background-color: #f0f2f5; margin: 0; padding: 20px; }
    .container { width: 900px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
    h2, h3 { color: #333; }
    
    .insert-box { background-color: #e3f2fd; padding: 15px; border-radius: 8px; margin-bottom: 30px; display: flex; align-items: center; gap: 15px; }
    .insert-box input { padding: 8px; border: 1px solid #ccc; border-radius: 4px; }
    .btn-submit { padding: 8px 15px; background-color: #2196F3; color: white; border: none; border-radius: 4px; cursor: pointer; font-weight: bold; }
    
    table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
    th, td { border: 1px solid #ddd; padding: 12px; text-align: center; }
    th { background-color: #f8f9fa; }
    
    .btn-edit { background-color: #FF9800; color: white; border: none; padding: 5px 10px; border-radius: 4px; cursor: pointer; }
    .btn-del { background-color: #f44336; color: white; border: none; padding: 5px 10px; border-radius: 4px; cursor: pointer; }
    .btn-home { display: inline-block; padding: 10px 20px; background-color: #607D8B; color: white; text-decoration: none; border-radius: 4px; font-weight: bold; }
    
    #updateModal { display: none; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background-color: white; padding: 20px; border-radius: 8px; box-shadow: 0 4px 15px rgba(0,0,0,0.2); z-index: 1000; width: 300px; }
    .modal-overlay { display: none; position: fixed; top: 0; left: 0; right: 0; bottom: 0; background-color: rgba(0,0,0,0.5); z-index: 999; }
    .update-group { margin-bottom: 15px; }
    .update-group input { width: 100%; padding: 8px; box-sizing: border-box; }
</style>
</head>
<body>

<div class="container">
    <h2>재고 관리 대시보드</h2>
    
    <div class="insert-box">
        <h3 style="margin: 0; margin-right: 20px;">신규 등록</h3>
        <form action="insertEq.do" method="post" style="display: flex; gap: 10px;">
            <input type="text" name="eqName" placeholder="비품명 입력" required>
            <input type="number" name="totalCount" placeholder="총 수량" required style="width: 100px;"> <!-- name 수정 -->
            <button type="submit" class="btn-submit">등록하기</button>
        </form>
    </div>

    <table>
        <thead>
            <tr>
                <th>번호</th>
                <th>비품명</th>
                <th>총 수량</th>
                <th>잔여 수량</th>
                <th>관리</th>
            </tr>
        </thead>
        <tbody>
            <% 
            if (eqList != null && !eqList.isEmpty()) {
                for (EquipmentDTO eq : eqList) { 
            %>
            <tr>
                <td><%= eq.getEqNo() %></td>
                <td><b><%= eq.getEqName() %></b></td>
                <td><%= eq.getTotalCount() %></td> <!-- DTO 메서드명 수정 -->
                <td><%= eq.getRemainCount() %></td> <!-- DTO 메서드명 수정 -->
                <td>
                    <button class="btn-edit" onclick="openUpdateModal('<%= eq.getEqNo() %>', '<%= eq.getEqName() %>', '<%= eq.getTotalCount() %>', '<%= eq.getRemainCount() %>')">수정</button>
                    <button class="btn-del" onclick="deleteEquipment('<%= eq.getEqNo() %>')">폐기</button>
                </td>
            </tr>
            <% 
                }
            } else { 
            %>
            <tr>
                <td colspan="5">등록된 비품이 없습니다.</td>
            </tr>
            <% } %>
        </tbody>
    </table>

    <a href="main.jsp" class="btn-home">메인으로 돌아가기</a>
</div>

<!-- 수정 모달 폼 -->
<div class="modal-overlay" id="modalOverlay" onclick="closeUpdateModal()"></div>
<div id="updateModal">
    <h3 style="margin-top:0;">비품 정보 수정</h3>
    <form action="updateEq.do" method="post">
        <input type="hidden" name="eqNo" id="upEqNo">
        <div class="update-group">
            <label>비품명</label>
            <input type="text" name="eqName" id="upEqName" required>
        </div>
        <div class="update-group">
            <label>총 수량</label>
            <input type="number" name="totalCount" id="upTotalCount" required> <!-- name 수정 -->
        </div>
        <div class="update-group">
            <label>잔여 수량</label>
            <input type="number" name="remainCount" id="upRemainCount" required> <!-- name 수정 -->
        </div>
        <div style="text-align: right;">
            <button type="button" class="btn-home" style="background-color: #9e9e9e; padding: 8px 15px;" onclick="closeUpdateModal()">취소</button>
            <button type="submit" class="btn-submit">수정 완료</button>
        </div>
    </form>
</div>

<!-- 폐기 폼 -->
<form id="deleteForm" action="deleteEq.do" method="post">
    <input type="hidden" name="eqNo" id="delEqNo">
</form>

<script>
    function deleteEquipment(eqNo) {
        if (confirm(eqNo + "번 비품을 정말 폐기하시겠습니까? (대여 중인 경우 폐기 불가)")) {
            document.getElementById("delEqNo").value = eqNo;
            document.getElementById("deleteForm").submit();
        }
    }

    function openUpdateModal(no, name, total, remain) {
        document.getElementById("upEqNo").value = no;
        document.getElementById("upEqName").value = name;
        document.getElementById("upTotalCount").value = total; // id 매칭
        document.getElementById("upRemainCount").value = remain; // id 매칭
        
        document.getElementById("updateModal").style.display = "block";
        document.getElementById("modalOverlay").style.display = "block";
    }

    function closeUpdateModal() {
        document.getElementById("updateModal").style.display = "none";
        document.getElementById("modalOverlay").style.display = "none";
    }
</script>

</body>
</html>