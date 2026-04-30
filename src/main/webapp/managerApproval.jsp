<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.office.dto.RentalHistoryDTO" %>
<%
    List<RentalHistoryDTO> approvalList = (List<RentalHistoryDTO>) request.getAttribute("approvalList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 관리자 결재함</title>
<style>
    body { font-family: 'Malgun Gothic', sans-serif; background-color: #f0f2f5; padding: 20px; }
    .container { background-color: white; padding: 30px; border-radius: 8px; width: 1000px; margin: 0 auto; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
    table { width: 100%; border-collapse: collapse; margin-top: 20px; text-align: center; }
    th, td { border: 1px solid #ddd; padding: 12px; }
    th { background-color: #673AB7; color: white; }
    .btn-approve { background-color: #4CAF50; color: white; padding: 6px 12px; text-decoration: none; border-radius: 4px; font-weight: bold; }
    .btn-reject { background-color: #f44336; color: white; padding: 6px 12px; text-decoration: none; border-radius: 4px; font-weight: bold; margin-left: 5px; }
    .sign-box { display: inline-block; width: 50px; height: 25px; line-height: 25px; border: 1px solid #ccc; font-size: 11px; background-color: #eee; margin: 0 2px; }
    .sign-done { background-color: #c8e6c9; color: #2e7d32; font-weight: bold; }
</style>
</head>
<body>

<div class="container">
    <h2>관리자 결재함 (5단계 시스템)</h2>
    <a href="main.jsp" style="display:inline-block; margin-bottom: 20px; color: #555; font-weight: bold;">[메인으로 돌아가기]</a>

    <table>
        <thead>
            <tr>
                <th>대여 번호</th>
                <th>사번</th>
                <th>현재 단계</th>
                <th>결재 현황 (1 ~ 5단계)</th>
                <th>액션</th>
            </tr>
        </thead>
        <tbody>
            <% if(approvalList == null || approvalList.isEmpty()) { %>
                <tr><td colspan="5">현재 결재 대기 중인 문서가 없습니다.</td></tr>
            <% } else { 
                for(RentalHistoryDTO dto : approvalList) { %>
                <tr>
                    <td><%= dto.getRentalNo() %></td>
                    <td><%= dto.getEmpNo() %></td>
                    <td><b><%= dto.getApprovalStep() %>단계 대기중</b></td>
                    <td>
                        <div class="sign-box <%= dto.getSign1() != null ? "sign-done" : "" %>"><%= dto.getSign1() != null ? "O" : "X" %></div>
                        <div class="sign-box <%= dto.getSign2() != null ? "sign-done" : "" %>"><%= dto.getSign2() != null ? "O" : "X" %></div>
                        <div class="sign-box <%= dto.getSign3() != null ? "sign-done" : "" %>"><%= dto.getSign3() != null ? "O" : "X" %></div>
                        <div class="sign-box <%= dto.getSign4() != null ? "sign-done" : "" %>"><%= dto.getSign4() != null ? "O" : "X" %></div>
                        <div class="sign-box <%= dto.getSign5() != null ? "sign-done" : "" %>"><%= dto.getSign5() != null ? "O" : "X" %></div>
                    </td>
                    <td>
                        <a href="approvalProcess.do?action=approve&rentalNo=<%= dto.getRentalNo() %>&approvalStep=<%= dto.getApprovalStep() %>" class="btn-approve">승인</a>
                        <a href="approvalProcess.do?action=reject&rentalNo=<%= dto.getRentalNo() %>&approvalStep=<%= dto.getApprovalStep() %>" class="btn-reject">반려</a>
                    </td>
                </tr>
            <%  }
               } %>
        </tbody>
    </table>
</div>

</body>
</html>