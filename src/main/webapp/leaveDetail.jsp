<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.office.dto.LeaveHistoryDTO"%>
<%
    // Controller에서 보낸 데이터를 받습니다.
    LeaveHistoryDTO doc = (LeaveHistoryDTO) request.getAttribute("doc");
    if (doc == null) {
        out.println("<script>alert('존재하지 않는 기안입니다.'); history.back();</script>");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사내 시스템 - 휴가 기안 상세</title>
<style>
    body { font-family: 'Segoe UI', sans-serif; background-color: #f4f6f9; padding: 40px; display: flex; justify-content: center; }
    .panel { background: white; padding: 40px; border-radius: 8px; width: 500px; border-top: 5px solid #28a745; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
    .info-row { margin-bottom: 15px; border-bottom: 1px solid #f1f1f1; padding-bottom: 10px; display: flex; }
    .label { width: 120px; font-weight: bold; color: #6c757d; }
    .btn-back { display: block; width: 100%; padding: 12px; margin-top: 30px; text-align: center; background: #f8f9fa; border: 1px solid #ced4da; text-decoration: none; border-radius: 4px; color: #343a40; font-weight: bold; }
</style>
</head>
<body>
    <div class="panel">
        <h2 style="margin-top:0;">휴가 신청서 상세</h2>
        <div class="info-row"><div class="label">문서 번호</div><div><%= doc.getLeaveNo() %></div></div>
        <div class="info-row"><div class="label">신청자</div><div><%= doc.getEmpName() %> (<%= doc.getDept() %>)</div></div>
        <div class="info-row"><div class="label">휴가 기간</div><div><%= doc.getStartDate() %> ~ <%= doc.getEndDate() %></div></div>
        <div class="info-row"><div class="label">사용 일수</div><div style="color:#d32f2f; font-weight:bold;"><%= doc.getUseDays() %>일</div></div>
        <div class="info-row"><div class="label">신청 사유</div><div><%= doc.getReason() %></div></div>
        <div class="info-row"><div class="label">현재 상태</div><div style="font-weight:bold;"><%= doc.getStatus() %></div></div>

        <a href="documentList.do" class="btn-back">기안 문서함으로 돌아가기</a>
    </div>
</body>
</html>