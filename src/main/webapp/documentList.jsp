<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.office.dto.RentalHistoryDTO" %>
<%@ page import="com.office.dto.EmployeeDTO" %>
<%
    // Controller에서 넘겨준 전체 기안 목록과 현재 로그인한 사용자 정보를 가져옵니다.
    List<RentalHistoryDTO> docList = (List<RentalHistoryDTO>) request.getAttribute("docList");
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>전자 결재 기안 문서함</title>
    <style>
        table { width: 100%; border-collapse: collapse; margin-top: 20px; font-size: 14px; }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: center; }
        th { background-color: #f8f9fa; font-weight: bold; }
        
        /* 링크가 걸린 텍스트 스타일 */
        .clickable-link { color: #0056b3; text-decoration: underline; cursor: pointer; font-weight: bold; }
        .non-clickable { color: #555; }
        
        /* 상태를 나타내는 작은 뱃지 디자인 */
        .status-badge { padding: 5px 10px; border-radius: 4px; font-weight: bold; display: inline-block; }
        .status-ing { background-color: #e8f5e9; color: #2e7d32; border: 1px solid #4caf50; }
        .status-done { background-color: #e3f2fd; color: #1565c0; border: 1px solid #2196f3; }
        .status-reject { background-color: #ffebee; color: #c62828; border: 1px solid #f44336; }
    </style>
</head>
<body>
    <h2>기안 문서함 (전체 현황)</h2>
    <a href="main.jsp" style="display:inline-block; margin-bottom:15px;">[메인 화면으로 돌아가기]</a>

    <table>
        <thead>
            <tr>
                <th>문서 번호</th>
                <th>기안일</th>
                <th>완료일</th>
                <th>기안 제목</th>
                <th>기안자 사번</th>
                <th>기안자 이름</th>
                <th>기안자 직급</th>
                <th>결재 상태</th>
            </tr>
        </thead>
        <tbody>
            <%
            if (docList != null && !docList.isEmpty()) {
                for (RentalHistoryDTO doc : docList) {
                    // 핵심 로직: 본인이 작성했거나, 본인의 직급이 기안자의 직급보다 높을 때만 true
                    boolean canView = false;
                    if (loginEmp != null) {
                        if ((loginEmp.getEmpNo() == doc.getEmpNo()) || (loginEmp.getEmpLevel() > doc.getEmpLevel())) {
                            canView = true;
                        }
                    }

                    // 완료일 표시: 5단계 최종 승인 날짜가 비어있지 않으면 출력, 없으면 '-' 표시
                    String finishDate = (doc.getSign5Date() != null) ? doc.getSign5Date().toString() : "-";
                    
                    // 기안 제목이 비어있을 경우의 기본값 처리
                    String docTitle = (doc.getTitle() != null && !doc.getTitle().isEmpty()) ? doc.getTitle() : "제목 없음";

                    // 상태에 따른 뱃지 색상 분기
                    String statusClass = "status-ing"; // 기본은 진행중(초록색)
                    if ("대여중".equals(doc.getStatus()) || "반납완료".equals(doc.getStatus())) {
                        statusClass = "status-done"; // 완료됨(파란색)
                    } else if ("반려됨".equals(doc.getStatus())) {
                        statusClass = "status-reject"; // 반려됨(빨간색)
                    }
            %>
                <tr>
                    <td><%= doc.getRentalNo() %></td>
                    <td><%= doc.getSign1Date() != null ? doc.getSign1Date() : doc.getRentalDate() %></td>
                    <td><%= finishDate %></td>
                    
                    <!-- 1. 기안 제목 영역 -->
                    <td>
                        <% if (canView) { %>
                            <!-- 열람 권한이 있으면 원본 폼을 보는 상세 페이지로 이동 -->
                            <a href="documentDetail.do?rentalNo=<%= doc.getRentalNo() %>" class="clickable-link">
                                <%= docTitle %>
                            </a>
                        <% } else { %>
                            <!-- 권한이 없으면 일반 텍스트로만 표시 -->
                            <span class="non-clickable"><%= docTitle %> <br><small>(열람 불가)</small></span>
                        <% } %>
                    </td>

                    <td><%= doc.getEmpNo() %></td>
                    <td><%= doc.getEmpName() %></td>
                    <td><%= doc.getEmpLevel() %>단계</td>
                    
                    <!-- 2. 결재 상태 영역 -->
                    <td>
                        <% if (canView) { %>
                            <!-- 열람 권한이 있으면 단계별 승인 상황 페이지로 이동 -->
                            <a href="approvalStatus.do?rentalNo=<%= doc.getRentalNo() %>" class="clickable-link status-badge <%= statusClass %>" style="text-decoration:none;">
                                <%= doc.getStatus() %>
                            </a>
                        <% } else { %>
                            <span class="status-badge <%= statusClass %>"><%= doc.getStatus() %></span>
                        <% } %>
                    </td>
                </tr>
            <%
                }
            } else {
            %>
                <tr>
                    <td colspan="8">현재 등록된 기안 문서가 없습니다.</td>
                </tr>
            <% } %>
        </tbody>
    </table>
</body>
</html>