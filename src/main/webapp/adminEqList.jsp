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
<title>사내 시스템 - 재고 관리</title>
<style>
    body { font-family: 'Segoe UI', 'Malgun Gothic', sans-serif; background-color: #f4f6f9; margin: 0; padding: 40px; }
    .container { max-width: 1000px; margin: 0 auto; }
    
    .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; border-bottom: 2px solid #343a40; padding-bottom: 15px; }
    .page-header h2 { margin: 0; color: #212529; font-size: 22px; }
    .btn-back { padding: 10px 18px; background-color: #ffffff; color: #343a40; text-decoration: none; border: 1px solid #ced4da; border-radius: 4px; font-weight: bold; font-size: 13px; transition: 0.2s; }
    .btn-back:hover { background-color: #e9ecef; }
    
    .insert-box { background-color: #ffffff; padding: 25px; border-radius: 6px; margin-bottom: 30px; display: flex; align-items: center; gap: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); border: 1px solid #e9ecef; border-left: 4px solid #343a40; }
    .insert-box h3 { margin: 0; color: #343a40; font-size: 16px; }
    .insert-box input { padding: 10px 12px; border: 1px solid #ced4da; border-radius: 4px; outline: none; font-family: inherit; }
    .insert-box input:focus { border-color: #495057; }
    .btn-submit { padding: 10px 20px; background-color: #343a40; color: white; border: none; border-radius: 4px; cursor: pointer; font-weight: bold; font-size: 13px; transition: 0.2s; }
    .btn-submit:hover { background-color: #212529; }

    .table-wrapper { background-color: #ffffff; border-radius: 6px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); overflow: hidden; border: 1px solid #e9ecef; }
    table { width: 100%; border-collapse: collapse; text-align: center; font-size: 14px; }
    th, td { border-bottom: 1px solid #e9ecef; padding: 14px 12px; vertical-align: middle; }
    th { background-color: #f8f9fa; color: #495057; font-weight: 600; border-bottom: 2px solid #dee2e6; }
    tr:last-child td { border-bottom: none; }
    tr:hover { background-color: #f8f9fa; }
    
    .btn-edit { background-color: #ffffff; color: #495057; border: 1px solid #ced4da; padding: 6px 14px; border-radius: 4px; cursor: pointer; font-size: 12px; font-weight: bold; margin-right: 5px; transition: 0.2s; }
    .btn-edit:hover { background-color: #f8f9fa; color: #212529; border-color: #adb5bd; }
    .btn-del { background-color: #ffffff; color: #dc3545; border: 1px solid #dc3545; padding: 6px 14px; border-radius: 4px; cursor: pointer; font-size: 12px; font-weight: bold; transition: 0.2s; }
    .btn-del:hover { background-color: #fff5f5; }

    /* 모달창 스타일 */
    .modal-overlay { display: none; position: fixed; top: 0; left: 0; right: 0; bottom: 0; background-color: rgba(33, 37, 41, 0.6); z-index: 999; }
    #updateModal { display: none; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background-color: #ffffff; padding: 35px; border-radius: 8px; box-shadow: 0 10px 30px rgba(0,0,0,0.1); z-index: 1000; width: 320px; border-top: 5px solid #343a40; }
    #updateModal h3 { margin-top: 0; margin-bottom: 25px; color: #212529; font-size: 18px; border-bottom: 1px solid #e9ecef; padding-bottom: 10px; }
    .update-group { margin-bottom: 20px; }
    .update-group label { display: block; margin-bottom: 8px; font-size: 13px; font-weight: 600; color: #495057; }
    .update-group input { width: 100%; padding: 10px; box-sizing: border-box; border: 1px solid #ced4da; border-radius: 4px; font-family: inherit; }
    .update-group input:focus { border-color: #495057; outline: none; }
    .modal-btn-group { display: flex; gap: 10px; margin-top: 30px; }
    .btn-modal-cancel { flex: 1; padding: 10px; background-color: #ffffff; border: 1px solid #ced4da; border-radius: 4px; font-weight: bold; color: #495057; cursor: pointer; }
    .btn-modal-cancel:hover { background-color: #f8f9fa; }
    .btn-modal-submit { flex: 1; padding: 10px; background-color: #343a40; color: white; border: none; border-radius: 4px; font-weight: bold; cursor: pointer; }
    .btn-modal-submit:hover { background-color: #212529; }
</style>
</head>
<body>

<div class="container">
    <div class="page-header">
        <h2>공용 비품 마스터 데이터 관리</h2>
        <a href="main.jsp" class="btn-back">시스템 메인으로</a>
    </div>
    
    <div class="insert-box">
        <h3>신규 비품 등록</h3>
        <form action="insertEq.do" method="post" style="display: flex; gap: 10px; flex: 1;">
            <input type="text" name="eqName" placeholder="비품 명칭 입력" required style="flex: 1;">
            <input type="number" name="totalCount" placeholder="초기 총 수량" required style="width: 120px;">
            <button type="submit" class="btn-submit">DB 등록</button>
        </form>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>관리 번호</th>
                    <th>비품 명칭</th>
                    <th>보유 총 수량</th>
                    <th>대여 가능 수량</th>
                    <th>데이터 관리</th>
                </tr>
            </thead>
            <tbody>
                <% if (eqList != null && !eqList.isEmpty()) {
                    for (EquipmentDTO eq : eqList) { 
                %>
                <tr>
                    <td style="color: #6c757d;"><%= eq.getEqNo() %></td>
                    <td style="font-weight: 600; color: #343a40;"><%= eq.getEqName() %></td>
                    <td><%= eq.getTotalCount() %> EA</td>
                    <td><strong style="color: <%= eq.getRemainCount() > 0 ? "#212529" : "#dc3545" %>;"><%= eq.getRemainCount() %> EA</strong></td>
                    <td>
                        <button class="btn-edit" onclick="openUpdateModal('<%= eq.getEqNo() %>', '<%= eq.getEqName() %>', '<%= eq.getTotalCount() %>', '<%= eq.getRemainCount() %>')">정보 수정</button>
                        <button class="btn-del" onclick="deleteEquipment('<%= eq.getEqNo() %>')">영구 폐기</button>
                    </td>
                </tr>
                <%  } } else { %>
                <tr><td colspan="5" style="padding: 40px; color: #6c757d;">시스템에 등록된 비품 마스터 데이터가 없습니다.</td></tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>

<div class="modal-overlay" id="modalOverlay" onclick="closeUpdateModal()"></div>
<div id="updateModal">
    <h3>비품 정보 수정</h3>
    <form action="updateEq.do" method="post">
        <input type="hidden" name="eqNo" id="upEqNo">
        <div class="update-group">
            <label>비품 명칭</label>
            <input type="text" name="eqName" id="upEqName" required>
        </div>
        <div class="update-group">
            <label>보유 총 수량</label>
            <input type="number" name="totalCount" id="upTotalCount" required>
        </div>
        <div class="update-group">
            <label>대여 가능 잔여 수량</label>
            <input type="number" name="remainCount" id="upRemainCount" required>
        </div>
        <div class="modal-btn-group">
            <button type="button" class="btn-modal-cancel" onclick="closeUpdateModal()">취소</button>
            <button type="submit" class="btn-modal-submit">수정 반영</button>
        </div>
    </form>
</div>

<form id="deleteForm" action="deleteEq.do" method="post">
    <input type="hidden" name="eqNo" id="delEqNo">
</form>

<script>
    function deleteEquipment(eqNo) {
        if (confirm("해당 비품 데이터를 시스템에서 영구 삭제하시겠습니까?\n(경고: 현재 사원이 대여 중인 비품은 삭제할 수 없습니다)")) {
            document.getElementById("delEqNo").value = eqNo;
            document.getElementById("deleteForm").submit();
        }
    }

    function openUpdateModal(no, name, total, remain) {
        document.getElementById("upEqNo").value = no;
        document.getElementById("upEqName").value = name;
        document.getElementById("upTotalCount").value = total;
        document.getElementById("upRemainCount").value = remain;
        
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