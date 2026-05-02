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
<title>사내 시스템 - 관리자 결재함</title>
<style>
    body { font-family: 'Segoe UI', 'Malgun Gothic', sans-serif; background-color: #f4f6f9; padding: 40px; margin: 0; }
    .container { background-color: #ffffff; padding: 40px; border-radius: 8px; max-width: 1100px; margin: 0 auto; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
    
    .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; border-bottom: 2px solid #343a40; padding-bottom: 15px; }
    .page-header h2 { margin: 0; color: #212529; font-size: 22px; }
    .btn-back { padding: 10px 18px; background-color: #ffffff; color: #343a40; text-decoration: none; border: 1px solid #ced4da; border-radius: 4px; font-weight: bold; font-size: 13px; transition: 0.2s; }
    .btn-back:hover { background-color: #e9ecef; }

    .table-wrapper { border-radius: 6px; overflow: hidden; border: 1px solid #e9ecef; }
    table { width: 100%; border-collapse: collapse; text-align: center; font-size: 14px; }
    th, td { border-bottom: 1px solid #e9ecef; padding: 14px 12px; }
    th { background-color: #f8f9fa; color: #495057; font-weight: 600; border-bottom: 2px solid #dee2e6; }
    tr:last-child td { border-bottom: none; }
    tr:hover { background-color: #f8f9fa; }
    
    .btn-action { padding: 6px 14px; text-decoration: none; border-radius: 4px; font-size: 12px; font-weight: bold; transition: 0.2s; display: inline-block; }
    .btn-approve { background-color: #343a40; color: white; border: 1px solid #343a40; }
    .btn-approve:hover { background-color: #212529; }
    .btn-reject { background-color: #ffffff; color: #dc3545; border: 1px solid #dc3545; margin-left: 5px; }
    .btn-reject:hover { background-color: #fff5f5; }
    
    .sign-wrapper { display: flex; justify-content: center; gap: 4px; }
    .sign-box { width: 45px; height: 26px; line-height: 26px; border: 1px solid #ced4da; font-size: 11px; background-color: #f8f9fa; color: #adb5bd; border-radius: 2px; }
    .sign-done { background-color: #e9ecef; color: #212529; font-weight: bold; border-color: #adb5bd; }
</style>
</head>
<body>

<div class="container">
    <div class="page-header">
        <h2>부서장 결재 수신함</h2>
        <a href="main.jsp" class="btn-back">시스템 메인으로</a>
    </div>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>문서 번호</th>
                    <th>기안자 사번</th>
                    <th>현재 단계</th>
                    <th>결재 진행 현황 (1~5단계)</th>
                    <th>결재 처리</th>
                </tr>
            </thead>
            <tbody>
                <% if(approvalList == null || approvalList.isEmpty()) { %>
                    <tr><td colspan="5" style="padding: 40px; color: #6c757d;">현재 결재 대기 중인 수신 문서가 없습니다.</td></tr>
                <% } else { 
                    for(RentalHistoryDTO dto : approvalList) { 
                %>
                    <tr>
                        <td style="color: #6c757d;"><%= dto.getRentalNo() %></td>
                        <td style="font-weight: 600;"><%= dto.getEmpNo() %></td>
                        <td><span style="color: #d32f2f; font-weight: bold;"><%= dto.getApprovalStep() %>단계 대기중</span></td>
                        <td>
                            <div class="sign-wrapper">
                                <div class="sign-box <%= dto.getSign1() != null ? "sign-done" : "" %>"><%= dto.getSign1() != null ? "완료" : "-" %></div>
                                <div class="sign-box <%= dto.getSign2() != null ? "sign-done" : "" %>"><%= dto.getSign2() != null ? "완료" : "-" %></div>
                                <div class="sign-box <%= dto.getSign3() != null ? "sign-done" : "" %>"><%= dto.getSign3() != null ? "완료" : "-" %></div>
                                <div class="sign-box <%= dto.getSign4() != null ? "sign-done" : "" %>"><%= dto.getSign4() != null ? "완료" : "-" %></div>
                                <div class="sign-box <%= dto.getSign5() != null ? "sign-done" : "" %>"><%= dto.getSign5() != null ? "완료" : "-" %></div>
                            </div>
                        </td>
                        <td>
                            <a href="approvalProcess.do?action=approve&rentalNo=<%= dto.getRentalNo() %>&approvalStep=<%= dto.getApprovalStep() %>" class="btn-action btn-approve" onclick="return confirm('승인하시겠습니까?');">승인</a>
                            <a href="approvalProcess.do?action=reject&rentalNo=<%= dto.getRentalNo() %>&approvalStep=<%= dto.getApprovalStep() %>" class="btn-action btn-reject" onclick="return confirm('반려하시겠습니까?');">반려</a>
                        </td>
                    </tr>
                <%  } } %>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>