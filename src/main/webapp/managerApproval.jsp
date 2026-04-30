<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%
    List<Map<String, String>> approvalList = (List<Map<String, String>>) request.getAttribute("approvalList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 관리자 결재함</title>
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
    .status-badge {
        background-color: #FFC107;
        color: white;
        padding: 4px 8px;
        border-radius: 4px;
        font-size: 12px;
        font-weight: bold;
    }
    .btn-approve {
        background-color: #4CAF50;
        color: white;
        border: none;
        padding: 6px 12px;
        border-radius: 4px;
        cursor: pointer;
        font-weight: bold;
    }
    .btn-reject {
        background-color: #f44336;
        color: white;
        border: none;
        padding: 6px 12px;
        border-radius: 4px;
        cursor: pointer;
        font-weight: bold;
    }
    .btn-approve:hover { background-color: #45a049; }
    .btn-reject:hover { background-color: #d32f2f; }
</style>
<script>
    function processApproval(rentalNo, action) {
        const actionText = action === 'approve' ? '승인' : '반려';
        if (confirm("해당 기안을 " + actionText + "하시겠습니까?")) {
            // 실제 구현 시 ApprovalProcessController 로 이동하는 폼 제출이나 AJAX 호출
            //alert(rentalNo + "번 기안이 " + actionText + " 처리되었습니다. (테스트)");
            location.href = 'approvalProcess.do?rentalNo=' + rentalNo + '&action=' + action;
        }
    }
</script>
</head>
<body>

    <div class="header">
        <h2>비품 대여 결재함 (관리자용)</h2>
        <div>
            <a href="main.jsp" style="text-decoration: none; color: #333; font-weight: bold;">[메인으로 돌아가기]</a>
        </div>
    </div>

    <div class="table-container">
        <table>
            <thead>
                <tr>
                    <th>기안 번호</th>
                    <th>신청자</th>
                    <th>비품명</th>
                    <th>대여 기간</th>
                    <th>상태</th>
                    <th>결재 처리</th>
                </tr>
            </thead>
            <tbody>
                <%
                    if (approvalList != null && !approvalList.isEmpty()) {
                        for (Map<String, String> req : approvalList) {
                %>
                <tr>
                    <td><%= req.get("rentalNo") %></td>
                    <td><b><%= req.get("empName") %></b></td>
                    <td><%= req.get("eqName") %></td>
                    <td><%= req.get("rentalDate") %> ~ <%= req.get("returnDate") %></td>
                    <td><span class="status-badge"><%= req.get("status") %></span></td>
                    <td>
                        <button class="btn-approve" onclick="processApproval('<%= req.get("rentalNo") %>', 'approve')">승인</button>
                        <button class="btn-reject" onclick="processApproval('<%= req.get("rentalNo") %>', 'reject')">반려</button>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="6" style="padding: 30px; color: #888;">대기 중인 결재 내역이 없습니다.</td>
                </tr>
                <%  } %>
            </tbody>
        </table>
    </div>

</body>
</html>