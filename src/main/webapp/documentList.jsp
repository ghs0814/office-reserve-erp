<%-- 
    수정 사항: 
    1. 페이지 상단에서 DB 정보를 다시 읽어 세션(연차 등) 실시간 갱신[cite: 32, 43]
    2. 휴가 기안 열람 권한을 'approvalStep'이 아닌 'empLevel' 기준으로 수정[cite: 9]
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.office.dto.RentalHistoryDTO"%>
<%@ page import="com.office.dto.LeaveHistoryDTO"%>
<%@ page import="com.office.dto.EmployeeDTO"%>
<%@ page import="com.office.dao.EmployeeDAO"%> <%-- ★ 실시간 갱신을 위해 추가 --%>
<%
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    if (loginEmp == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    // ★ [해결 1] 실시간 연차 반영: 페이지 열 때마다 DB에서 최신 정보를 읽어 세션 갱신
    EmployeeDAO empDao = new EmployeeDAO();
    EmployeeDTO updatedEmp = empDao.getEmployeeByNo(String.valueOf(loginEmp.getEmpNo()));
    if (updatedEmp != null) {
        session.setAttribute("loginEmp", updatedEmp);
        loginEmp = updatedEmp; 
    }

    List<RentalHistoryDTO> docList = (List<RentalHistoryDTO>) request.getAttribute("docList");
    List<LeaveHistoryDTO> leaveDocList = (List<LeaveHistoryDTO>) request.getAttribute("leaveDocList");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>사내 시스템 - 통합 기안 문서함</title>
    <style>
        body { font-family: 'Segoe UI', 'Malgun Gothic', sans-serif; background-color: #f4f6f9; padding: 40px; margin: 0; }
        .container { max-width: 1200px; margin: 0 auto; }
        .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; border-bottom: 2px solid #343a40; padding-bottom: 15px; }
        .page-header h2 { margin: 0; color: #212529; font-size: 22px; font-weight: 800; }
        .btn-back { padding: 10px 18px; background-color: #ffffff; color: #343a40; text-decoration: none; border: 1px solid #ced4da; border-radius: 4px; font-weight: bold; font-size: 13px; transition: 0.2s; }
        .btn-back:hover { background-color: #e9ecef; }
        .table-wrapper { background-color: #ffffff; border-radius: 6px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); overflow: hidden; border: 1px solid #e9ecef; margin-bottom: 50px; }
        table { width: 100%; border-collapse: collapse; text-align: center; font-size: 14px; }
        th, td { padding: 14px 12px; border-bottom: 1px solid #e9ecef; }
        th { background-color: #f8f9fa; color: #495057; font-weight: 600; border-bottom: 2px solid #dee2e6; }
        tr:last-child td { border-bottom: none; }
        tr:hover { background-color: #f8f9fa; }
        .clickable-link { color: #212529; text-decoration: underline; cursor: pointer; font-weight: 600; transition: color 0.2s; }
        .clickable-link:hover { color: #495057; }
        .non-clickable { color: #adb5bd; }
        .status-badge { padding: 5px 10px; border-radius: 4px; font-weight: bold; font-size: 12px; display: inline-block; text-decoration: none; }
        .status-ing { background-color: #f8f9fa; color: #495057; border: 1px solid #ced4da; }
        .status-done { background-color: #e8f5e9; color: #2e7d32; border: 1px solid #c8e6c9; }
        .status-reject { background-color: #ffebee; color: #c62828; border: 1px solid #ffcdd2; }
    </style>
</head>
<body>
<div class="container">

    <!-- 1. 비품 대여 기안 영역 -->
    <div class="page-header">
        <h2>전사 비품 대여 문서함</h2>
        <a href="main.jsp" class="btn-back">시스템 메인으로</a>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>문서 번호</th><th>기안 일자</th><th>완료 일자</th><th>기안 제목</th><th>기안자</th><th>직급</th><th>결재 상태</th>
                </tr>
            </thead>
            <tbody>
                <% if (docList != null && !docList.isEmpty()) {
                    for (RentalHistoryDTO doc : docList) {
                        // 권한 체크: 내 문서이거나 내 직급이 기안자보다 높을 때[cite: 9]
                        boolean canView = (loginEmp.getEmpNo() == doc.getEmpNo()) || (loginEmp.getEmpLevel() > doc.getEmpLevel());
                        String finishDate = (doc.getSign5Date() != null) ? doc.getSign5Date().toString() : "-";
                        String statusClass = "status-ing";
                        if ("대여중".equals(doc.getStatus()) || "반납완료".equals(doc.getStatus())) statusClass = "status-done";
                        else if ("반려됨".equals(doc.getStatus())) statusClass = "status-reject";
                %>
                    <tr>
                        <td style="color: #6c757d;"><%= doc.getRentalNo() %></td>
                        <td><%= doc.getSign1Date() != null ? doc.getSign1Date() : doc.getRentalDate() %></td>
                        <td style="color: #6c757d;"><%= finishDate %></td>
                        <td style="text-align: left; padding-left: 20px;">
                            <% if (canView) { %>
                                <a href="documentDetail.do?rentalNo=<%= doc.getRentalNo() %>" class="clickable-link"><%= (doc.getTitle() != null) ? doc.getTitle() : "제목 없음" %></a>
                            <% } else { %>
                                <span class="non-clickable">비공개 (열람 권한 없음)</span>
                            <% } %>
                        </td>
                        <td><%= doc.getEmpName() %></td>
                        <td><%= doc.getEmpLevel() %>단계</td>
                        <td>
                            <% if (canView) { %>
                                <a href="approvalStatus.do?rentalNo=<%= doc.getRentalNo() %>" class="status-badge <%= statusClass %>"><%= doc.getStatus() %></a>
                            <% } else { %>
                                <span class="status-badge <%= statusClass %>"><%= doc.getStatus() %></span>
                            <% } %>
                        </td>
                    </tr>
                <% } } else { %>
                    <tr><td colspan="7" style="padding: 40px; color: #6c757d;">등록된 대여 기안이 없습니다.</td></tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <hr style="border: 0; height: 1px; background: #dee2e6; margin-bottom: 50px;">

    <!-- 2. 휴가 신청 기안 영역 -->
    <div class="page-header">
        <h2>전사 휴가 신청 문서함</h2>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>문서 번호</th><th>기간</th><th>사용 일수</th><th>기안자</th><th>부서</th><th>사유</th><th>결재 상태</th>
                </tr>
            </thead>
            <tbody>
                <% if (leaveDocList != null && !leaveDocList.isEmpty()) {
                    for (LeaveHistoryDTO leave : leaveDocList) {
                        // ★ [해결 2] 휴가 열람 권한: 'approvalStep' 대신 기안자의 실제 직급 'empLevel' 비교
                        boolean canView = (loginEmp.getEmpNo() == leave.getEmpNo()) || (loginEmp.getEmpLevel() > leave.getEmpLevel());
                        
                        String statusClass = "status-ing";
                        if ("승인완료".equals(leave.getStatus())) statusClass = "status-done";
                        else if ("반려됨".equals(leave.getStatus())) statusClass = "status-reject";
                %>
                    <tr>
                        <td style="color: #6c757d;"><%= leave.getLeaveNo() %></td>
                        <td><%= leave.getStartDate() %> ~ <%= leave.getEndDate() %></td>
                        <td><b style="color: #343a40;"><%= leave.getUseDays() %>일</b></td>
                        <td><%= leave.getEmpName() %></td>
                        <td><%= leave.getDept() %></td>
                        <td style="text-align: left; padding-left: 20px;">
                            <% if (canView) { %>
                                <a href="leaveDetail.do?leaveNo=<%= leave.getLeaveNo() %>" class="clickable-link"><%= leave.getReason() %></a>
                            <% } else { %>
                                <span class="non-clickable">비공개 (열람 권한 없음)</span>
                            <% } %>
                        </td>
                        <td>
                            <% if (canView) { %>
                                <a href="leaveStatus.do?leaveNo=<%= leave.getLeaveNo() %>" class="status-badge <%= statusClass %>"><%= leave.getStatus() %></a>
                            <% } else { %>
                                <span class="status-badge <%= statusClass %>"><%= leave.getStatus() %></span>
                            <% } %>
                        </td>
                    </tr>
                <% } } else { %>
                    <tr><td colspan="7" style="padding: 40px; color: #6c757d;">등록된 휴가 기안이 없습니다.</td></tr>
                <% } %>
            </tbody>
        </table>
    </div>

</div>
</body>
</html>