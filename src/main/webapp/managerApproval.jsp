<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.office.dto.RentalHistoryDTO" %>
<%@ page import="com.office.dto.LeaveHistoryDTO" %>
<%
    List<RentalHistoryDTO> approvalList = (List<RentalHistoryDTO>) request.getAttribute("approvalList");
    List<LeaveHistoryDTO> leaveList = (List<LeaveHistoryDTO>) request.getAttribute("leaveList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사내 시스템 - 관리자 결재함</title>
<style>
    body { font-family: 'Segoe UI', 'Malgun Gothic', sans-serif; background-color: #f4f6f9; padding: 40px; margin: 0; }
    .container { background-color: #ffffff; padding: 40px; border-radius: 8px; max-width: 1100px; margin: 0 auto; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
    .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; border-bottom: 2px solid #343a40; padding-bottom: 15px; }
    .page-header h2 { margin: 0; color: #212529; font-size: 22px; }
    .section-title { font-size: 18px; font-weight: bold; margin: 30px 0 15px 0; color: #343a40; border-left: 5px solid #212529; padding-left: 10px; }
    .table-wrapper { border-radius: 6px; overflow: hidden; border: 1px solid #e9ecef; }
    table { width: 100%; border-collapse: collapse; text-align: center; font-size: 14px; }
    th, td { border-bottom: 1px solid #e9ecef; padding: 14px 12px; }
    th { background-color: #f8f9fa; color: #495057; font-weight: 600; border-bottom: 2px solid #dee2e6; }
    .btn-action { padding: 6px 14px; text-decoration: none; border-radius: 4px; font-size: 12px; font-weight: bold; display: inline-block; }
    .btn-approve { background-color: #343a40; color: white; border: 1px solid #343a40; }
    .btn-reject { background-color: #ffffff; color: #dc3545; border: 1px solid #dc3545; margin-left: 5px; }
    .sign-box { width: 45px; height: 26px; line-height: 26px; border: 1px solid #ced4da; font-size: 11px; background-color: #f8f9fa; color: #adb5bd; border-radius: 2px; display: inline-block; }
    .sign-done { background-color: #e9ecef; color: #212529; font-weight: bold; border-color: #adb5bd; }
</style>
</head>
<body>
<div class="container">
    <div class="page-header">
        <h2>부서장 결재 수신함</h2>
        <a href="main.jsp" style="text-decoration:none; color:#343a40; font-weight:bold;">메인으로</a>
    </div>

    <!-- 1. 비품 대여 결재 목록 -->
    <div class="section-title">비품 대여 신청 건</div>
    <div class="table-wrapper">
        <table>
            <thead>
                <tr><th>문서 번호</th><th>기안자</th><th>단계</th><th>진행 현황</th><th>처리</th></tr>
            </thead>
            <tbody>
                <% if(approvalList == null || approvalList.isEmpty()) { %>
                    <tr><td colspan="5" style="padding: 30px;">대기 중인 비품 기안이 없습니다.</td></tr>
                <% } else { for(RentalHistoryDTO r : approvalList) { %>
                    <tr>
                        <td><%= r.getRentalNo() %></td><td><%= r.getEmpNo() %></td>
                        <td><b style="color:#d32f2f;"><%= r.getApprovalStep() %>단계</b></td>
                        <td>
                            <div class="sign-box <%= r.getSign1() != null ? "sign-done" : "" %>">1</div>
                            <div class="sign-box <%= r.getSign2() != null ? "sign-done" : "" %>">2</div>
                            <div class="sign-box <%= r.getSign3() != null ? "sign-done" : "" %>">3</div>
                            <div class="sign-box <%= r.getSign4() != null ? "sign-done" : "" %>">4</div>
                            <div class="sign-box <%= r.getSign5() != null ? "sign-done" : "" %>">5</div>
                        </td>
                        <td>
                            <a href="approvalProcess.do?action=approve&rentalNo=<%= r.getRentalNo() %>&approvalStep=<%= r.getApprovalStep() %>" class="btn-action btn-approve">승인</a>
                            <a href="approvalProcess.do?action=reject&rentalNo=<%= r.getRentalNo() %>&approvalStep=<%= r.getApprovalStep() %>" class="btn-action btn-reject">반려</a>
                        </td>
                    </tr>
                <% } } %>
            </tbody>
        </table>
    </div>

    <!-- 2. 휴가 신청 결재 목록 -->
    <div class="section-title">휴가 신청 건</div>
    <div class="table-wrapper">
        <table>
            <thead>
                <tr><th>문서 번호</th><th>기안자</th><th>단계</th><th>사용 일수</th><th>처리</th></tr>
            </thead>
            <tbody>
                <% if(leaveList == null || leaveList.isEmpty()) { %>
                    <tr><td colspan="5" style="padding: 30px;">대기 중인 휴가 기안이 없습니다.</td></tr>
                <% } else { for(LeaveHistoryDTO l : leaveList) { %>
                    <tr>
                        <td><%= l.getLeaveNo() %></td><td><%= l.getEmpName() %>(<%= l.getDept() %>)</td>
                        <td><b style="color:#d32f2f;"><%= l.getApprovalStep() %>단계</b></td>
                        <td><b><%= l.getUseDays() %>일</b></td>
                        <td>
                            <a href="leaveApproveAction.do?action=approve&leaveNo=<%= l.getLeaveNo() %>&approvalStep=<%= l.getApprovalStep() %>" class="btn-action btn-approve">승인</a>
                            <a href="leaveApproveAction.do?action=reject&leaveNo=<%= l.getLeaveNo() %>&approvalStep=<%= l.getApprovalStep() %>" class="btn-action btn-reject">반려</a>
                        </td>
                    </tr>
                <% } } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>