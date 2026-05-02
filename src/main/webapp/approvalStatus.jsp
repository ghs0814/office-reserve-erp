<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.office.dto.RentalHistoryDTO" %>
<%@ page import="com.office.dto.EmployeeDTO" %>
<%
    RentalHistoryDTO doc = (RentalHistoryDTO) request.getAttribute("docDetail");
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");

    if (doc == null) {
        out.println("<script>alert('존재하지 않거나 삭제된 기안입니다.'); history.back();</script>");
        return;
    }

    boolean canApprove = "승인대기".equals(doc.getStatus()) && (doc.getApprovalStep() == loginEmp.getEmpLevel());
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>전자 결재 기안 상세</title>
    <style>
        body { font-family: 'Malgun Gothic', sans-serif; background-color: #f4f6f9; padding: 20px; }
        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        .approval-line { display: flex; justify-content: flex-end; margin-bottom: 30px; border-bottom: 2px solid #333; padding-bottom: 20px; }
        .sign-box { width: 100px; border: 1px solid #ccc; text-align: center; margin-left: -1px; }
        .sign-title { background-color: #f0f0f0; border-bottom: 1px solid #ccc; padding: 5px; font-weight: bold; font-size: 13px; }
        .sign-name { height: 50px; line-height: 50px; font-weight: bold; color: #2196F3; }
        .sign-date { border-top: 1px solid #eee; font-size: 11px; padding: 3px; color: #666; height: 16px; }
        .content-section { display: flex; justify-content: space-between; gap: 20px; }
        .panel { flex: 1; border: 1px solid #ddd; border-radius: 4px; padding: 20px; }
        .panel h3 { margin-top: 0; border-bottom: 1px solid #eee; padding-bottom: 10px; color: #333; }
        .info-row { margin-bottom: 12px; }
        .info-label { font-weight: bold; display: inline-block; width: 100px; color: #555; }
        .action-buttons { text-align: center; margin-top: 30px; }
        .btn { padding: 10px 30px; border: none; border-radius: 4px; font-size: 16px; font-weight: bold; cursor: pointer; color: white; margin: 0 5px; }
        .btn-approve { background-color: #4CAF50; }
        .btn-reject { background-color: #f44336; }
        .btn-back { background-color: #607D8B; }
    </style>
</head>
<body>
    <div class="container">
        <h2 style="margin-top:0;">전자 결재 문서 [기안번호: <%= doc.getRentalNo() %>]</h2>
        <span style="display:inline-block; margin-bottom:20px; font-size:18px;">
            <b>상태:</b> <%= doc.getStatus() %>
        </span>

        <div class="approval-line">
            <% 
            String[] signs = {doc.getSign1(), doc.getSign2(), doc.getSign3(), doc.getSign4(), doc.getSign5()};
            java.sql.Date[] dates = {doc.getSign1Date(), doc.getSign2Date(), doc.getSign3Date(), doc.getSign4Date(), doc.getSign5Date()};
            
            for(int i=0; i<5; i++) { 
                String name = (signs[i] != null) ? signs[i] : "";
                String dateStr = (dates[i] != null) ? dates[i].toString() : "";
            %>
                <div class="sign-box">
                    <div class="sign-title"><%= (i+1) %>단계</div>
                    <div class="sign-name"><%= name %></div>
                    <div class="sign-date"><%= dateStr %></div>
                </div>
            <% } %>
        </div>

        <div class="content-section">
            <div class="panel">
                <h3>신청 비품 현황</h3>
                <div class="info-row"><span class="info-label">비품 번호:</span> <%= doc.getEqNo() %></div>
                <div class="info-row"><span class="info-label">비품명:</span> <%= doc.getEqName() %></div>
                <div class="info-row"><span class="info-label">총 수량:</span> <%= doc.getTotalCount() %> 개</div>
                <div class="info-row"><span class="info-label">현재 수량:</span> 
                    <!-- ★ 수정 완료: getCurrentCount를 getRemainCount로 변경! -->
                    <span style="color: <%= doc.getRemainCount() > 0 ? "blue" : "red" %>; font-weight: bold;">
                        <%= doc.getRemainCount() %> 개
                    </span>
                </div>
            </div>

            <div class="panel">
                <h3>기안 상세 내용</h3>
                <div class="info-row"><span class="info-label">기안 제목:</span> <%= doc.getTitle() != null ? doc.getTitle() : "제목 없음" %></div>
                <div class="info-row"><span class="info-label">기안자:</span> <%= doc.getEmpName() %> (<%= doc.getEmpLevel() %>단계)</div>
                <div class="info-row"><span class="info-label">대여 신청일:</span> <%= doc.getRentalDate() %></div>
                <div class="info-row"><span class="info-label">반납 예정일:</span> <%= doc.getReturnDate() %></div>
            </div>
        </div>

        <div class="action-buttons">
            <button type="button" class="btn btn-back" onclick="location.href='documentList.do'">목록으로</button>
            
            <% if (canApprove) { %>
                <form action="approveProcess.do" method="post" style="display:inline-block;">
                    <input type="hidden" name="rentalNo" value="<%= doc.getRentalNo() %>">
                    <input type="hidden" name="currentStep" value="<%= doc.getApprovalStep() %>">
                    <input type="hidden" name="eqNo" value="<%= doc.getEqNo() %>">
                    
                    <button type="submit" name="action" value="approve" class="btn btn-approve" onclick="return confirm('이 기안을 승인하시겠습니까?');">승인</button>
                    <button type="submit" name="action" value="reject" class="btn btn-reject" onclick="return confirm('이 기안을 반려하시겠습니까?');">반려</button>
                </form>
            <% } %>
        </div>
    </div>
</body>
</html>