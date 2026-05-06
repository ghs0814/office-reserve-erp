<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.office.dto.LeaveHistoryDTO" %>
<%@ page import="com.office.dto.EmployeeDTO" %>
<%
    LeaveHistoryDTO doc = (LeaveHistoryDTO) request.getAttribute("doc");
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");

    if (doc == null) {
        out.println("<script>alert('존재하지 않는 기안입니다.'); history.back();</script>");
        return;
    }

    // 본인 직급과 결재 단계가 일치하고 상태가 승인대기일 때만 버튼 노출
    boolean canApprove = "승인대기".equals(doc.getStatus()) && (doc.getApprovalStep() == loginEmp.getEmpLevel());
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>사내 시스템 - 휴가 결재</title>
    <style>
        body { font-family: 'Segoe UI', 'Malgun Gothic', sans-serif; background-color: #f4f6f9; padding: 40px; margin: 0; }
        .container { max-width: 900px; margin: 0 auto; background: #ffffff; padding: 40px; border-radius: 8px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); border-top: 5px solid #28a745; }
        
        .header-title { display: flex; justify-content: space-between; align-items: flex-end; border-bottom: 2px solid #e9ecef; padding-bottom: 20px; margin-bottom: 30px; }
        .header-title h2 { margin: 0; color: #212529; font-size: 24px; }
        .status-text { font-size: 15px; color: #495057; font-weight: 600; background-color: #f8f9fa; padding: 6px 12px; border-radius: 4px; border: 1px solid #dee2e6; }

        .approval-line { display: flex; justify-content: flex-end; margin-bottom: 40px; }
        .sign-box { width: 110px; border: 1px solid #ced4da; text-align: center; margin-left: -1px; }
        .sign-title { background-color: #f8f9fa; border-bottom: 1px solid #ced4da; padding: 8px 5px; font-weight: 600; font-size: 13px; color: #495057; }
        .sign-name { height: 60px; line-height: 60px; font-weight: bold; color: #212529; font-size: 15px; }
        .sign-date { border-top: 1px solid #e9ecef; font-size: 11px; padding: 5px; color: #adb5bd; height: 16px; background-color: #ffffff; }

        .content-section { border: 1px solid #e9ecef; border-radius: 6px; padding: 25px; background-color: #fcfcfc; }
        .content-section h3 { margin-top: 0; border-bottom: 2px solid #e9ecef; padding-bottom: 12px; color: #343a40; font-size: 16px; }
        .info-row { margin-bottom: 14px; font-size: 14px; color: #212529; }
        .info-label { font-weight: 600; display: inline-block; width: 110px; color: #6c757d; }
        
        .action-buttons { text-align: center; margin-top: 40px; padding-top: 20px; border-top: 1px solid #e9ecef; }
        .btn { padding: 12px 30px; border: none; border-radius: 4px; font-size: 14px; font-weight: bold; cursor: pointer; margin: 0 5px; transition: 0.2s; }
        .btn-approve { background-color: #28a745; color: white; }
        .btn-reject { background-color: #ffffff; color: #dc3545; border: 1px solid #dc3545; }
        .btn-back { background-color: #f8f9fa; color: #495057; border: 1px solid #ced4da; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header-title">
            <h2>전자 결재 - 휴가 신청 [문서번호: <%= doc.getLeaveNo() %>]</h2>
            <div class="status-text">상태: <%= doc.getStatus() %></div>
        </div>

        <div class="approval-line">
            <% 
            String[] signs = {doc.getSign1(), doc.getSign2(), doc.getSign3(), doc.getSign4(), doc.getSign5()};
            java.sql.Date[] dates = {doc.getSign1Date(), doc.getSign2Date(), doc.getSign3Date(), doc.getSign4Date(), doc.getSign5Date()};
            
            for(int i=0; i<5; i++) { 
                String name = (signs[i] != null) ? signs[i] : "";
                String dateStr = (dates[i] != null) ? dates[i].toString() : "";
            %>
                <div class="sign-box">
                    <div class="sign-title">결재 <%= (i+1) %>단계</div>
                    <div class="sign-name"><%= name %></div>
                    <div class="sign-date"><%= dateStr %></div>
                </div>
            <% } %>
        </div>

        <div class="content-section">
            <h3>기안 상세 정보</h3>
            <div class="info-row"><span class="info-label">기안자</span> <%= doc.getEmpName() %> (<%= doc.getDept() %>)</div>
            <div class="info-row"><span class="info-label">휴가 기간</span> <%= doc.getStartDate() %> ~ <%= doc.getEndDate() %></div>
            <div class="info-row"><span class="info-label">실제 사용일</span> <strong style="color:#d32f2f;"><%= doc.getUseDays() %>일</strong> (평일 기준)</div>
            <div class="info-row"><span class="info-label">휴가 사유</span> <%= doc.getReason() %></div>
        </div>

        <div class="action-buttons">
            <button type="button" class="btn btn-back" onclick="location.href='documentList.do'">문서함으로</button>
            
            <% if (canApprove) { %>
                <form action="leaveApproveProcess.do" method="post" style="display:inline-block;">
                    <input type="hidden" name="leaveNo" value="<%= doc.getLeaveNo() %>">
                    <input type="hidden" name="currentStep" value="<%= doc.getApprovalStep() %>">
                    <button type="submit" name="action" value="approve" class="btn btn-approve" onclick="return confirm('이 휴가 신청을 승인하시겠습니까?');">최종 승인</button>
                    <button type="submit" name="action" value="reject" class="btn btn-reject" onclick="return confirm('이 휴가 신청을 반려하시겠습니까?');">반려 처리</button>
                </form>
            <% } %>
        </div>
    </div>
</body>
</html>