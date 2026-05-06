<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.office.dto.RentalHistoryDTO"%>
<%@ page import="com.office.dto.ReservationDTO"%>
<%@ page import="com.office.dto.LeaveHistoryDTO"%>
<%@ page import="com.office.dto.EmployeeDTO"%>
<%@ page import="com.office.dao.ReservationDAO"%>
<%@ page import="com.office.dao.RentalDAO"%>
<%@ page import="com.office.dao.LeaveDAO"%>
<%@ page import="com.office.dao.EmployeeDAO"%>
<%@ page import="java.time.LocalDateTime"%>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.time.LocalTime"%>
<%
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    if (loginEmp == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    // 실시간 연차 갱신 로직
    EmployeeDAO empDao = new EmployeeDAO();
    EmployeeDTO updatedEmp = empDao.getEmployeeByNo(String.valueOf(loginEmp.getEmpNo()));
    if (updatedEmp != null) {
        session.setAttribute("loginEmp", updatedEmp);
        loginEmp = updatedEmp;
    }

    // 데이터 가져오기
    ReservationDAO resDao = new ReservationDAO();
    List<ReservationDTO> reserveList = resDao.getMyReservations(loginEmp.getEmpNo());

    RentalDAO rentalDao = new RentalDAO();
    List<RentalHistoryDTO> myList = rentalDao.getMyRentalList(loginEmp.getEmpNo());

    LeaveDAO leaveDao = new LeaveDAO();
    List<LeaveHistoryDTO> myLeaveList = leaveDao.getMyLeaveList(loginEmp.getEmpNo());
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사내 시스템 - 메인 대시보드</title>
<style>
    body { font-family: 'Segoe UI', 'Malgun Gothic', sans-serif; background-color: #f4f6f9; margin: 0; padding: 30px; }
    .header { display: flex; justify-content: space-between; align-items: center; background-color: #212529; color: #ffffff; padding: 15px 30px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); margin-bottom: 30px; }
    .header h2 { margin: 0; font-size: 20px; letter-spacing: 1px; cursor: pointer; }
    .nav-buttons { display: flex; align-items: center; gap: 10px; }
    .nav-btn { padding: 8px 16px; background-color: #343a40; color: #ffffff; text-decoration: none; border-radius: 4px; font-weight: 600; font-size: 13px; border: 1px solid #495057; transition: all 0.2s; }
    .nav-btn:hover { background-color: #495057; border-color: #6c757d; }
    .nav-btn.admin { border-color: #dc3545; color: #ffc107; }
    .nav-btn.admin:hover { background-color: #dc3545; color: #ffffff; }
    .nav-btn.logout { background-color: transparent; border: none; color: #adb5bd; text-decoration: underline; }
    .nav-btn.logout:hover { color: #ffffff; }

    .dashboard-container { max-width: 1000px; margin: 0 auto; }
    .section-title { font-size: 18px; font-weight: bold; margin: 0 0 15px 0; color: #343a40; display: flex; align-items: center; }
    .section-title::before { content: ''; display: inline-block; width: 4px; height: 18px; background-color: #6c757d; margin-right: 10px; }
    .table-wrapper { background-color: #ffffff; border-radius: 6px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04); margin-bottom: 40px; overflow: hidden; border: 1px solid #e9ecef; }
    table { width: 100%; border-collapse: collapse; text-align: center; font-size: 14px; }
    th, td { padding: 14px 12px; border-bottom: 1px solid #e9ecef; }
    th { background-color: #f8f9fa; color: #495057; font-weight: 600; border-bottom: 2px solid #dee2e6; }
    .status-badge { padding: 5px 10px; border-radius: 4px; font-weight: bold; font-size: 12px; display: inline-block; }
    .bg-warning { background-color: #fff8e1; color: #f57c00; border: 1px solid #ffe0b2; }
    .bg-success { background-color: #e8f5e9; color: #2e7d32; border: 1px solid #c8e6c9; }
    .bg-danger { background-color: #ffebee; color: #c62828; border: 1px solid #ffcdd2; }
    .bg-primary { background-color: #e3f2fd; color: #1565c0; border: 1px solid #bbdefb; }
    .bg-secondary { background-color: #f1f3f5; color: #495057; border: 1px solid #dee2e6; }
    .btn-action { padding: 6px 14px; border-radius: 4px; cursor: pointer; font-weight: bold; font-size: 12px; transition: all 0.2s; }
    .btn-return { background-color: #343a40; color: #ffffff; border: 1px solid #343a40; }
    .btn-cancel { background-color: #ffffff; color: #dc3545; border: 1px solid #dc3545; }
</style>
<script>
    function processReturn(rentalNo) { if (confirm("반납 처리하시겠습니까?")) location.href = 'returnProcess.do?rentalNo=' + rentalNo + '&from=main'; }
    function cancelReserve(resNo) { if (confirm("예약을 취소하시겠습니까?")) location.href = "cancelReserve.do?resNo=" + resNo + '&from=main'; }
</script>
</head>
<body>

    <div class="header">
        <%-- ★ 로고 클릭 시 메인 새로고침 --%>
        <a href="main.jsp" style="text-decoration: none; color: inherit;"><h2>Groupware</h2></a>
        <div class="nav-buttons">
            <%-- ★ [관리자]와 성함이 나란히 보이도록 묶음 --%>
            <span style="margin-right: 20px; font-size: 14px; color: #e9ecef;">
                <% if ("Y".equals(loginEmp.getManager())) { %>
                    <span style="color: #ffc107; font-weight: bold; font-size: 13px; margin-right: 5px;">[관리자]</span>
                <% } %>
                <b><%=loginEmp.getEmpName()%></b>님
            </span>

            <% if ("Y".equals(loginEmp.getManager())) { %>
                <span style="color: #495057;">|</span>
                <a href="adminEqList.do" class="nav-btn admin">재고 관리</a>
                <a href="admin.do" class="nav-btn admin">사원 관리</a>
                <span style="color: #495057;">|</span>
            <% } %>

            <a href="officeMap.jsp" class="nav-btn">오피스 예약</a>
            <a href="leaveForm.do" class="nav-btn">휴가 신청</a>
            <a href="equipmentList.do" class="nav-btn">비품 대여 신청</a>
            <a href="documentList.do" class="nav-btn">기안 문서함</a>
            <a href="myPage.do" class="nav-btn">마이페이지</a>
            <a href="logout.do" class="nav-btn logout">로그아웃</a>
        </div>
    </div>

    <div class="dashboard-container">
        <!-- 상단 카드 -->
        <div style="background: white; padding: 20px; border-radius: 8px; margin-bottom: 30px; display: flex; align-items: center; justify-content: space-between; border: 1px solid #e9ecef;">
            <div><h3 style="margin: 0; color: #343a40;">안녕하세요, <b><%=loginEmp.getEmpName()%></b>님!</h3><p style="margin: 5px 0 0 0; color: #6c757d; font-size: 14px;"><%=loginEmp.getDept()%> 소속</p></div>
            <div style="text-align: right;"><p style="margin: 0; font-size: 13px; color: #6c757d;">잔여 연차 현황</p><h2 style="margin: 0; color: #28a745;"><%=loginEmp.getCurLeave()%> <span style="font-size: 16px; color: #adb5bd;">/ <%=loginEmp.getMaxLeave()%></span></h2></div>
        </div>

        <!-- 1. 회의실 현황 -->
        <div class="section-title">내 회의실 예약 현황</div>
        <div class="table-wrapper">
            <table>
                <thead><tr><th>예약 번호</th><th>회의실</th><th>예약 일자</th><th>사용 시간</th><th>사용 목적</th><th>상태</th><th>비고</th></tr></thead>
                <tbody>
                <% if (reserveList == null || reserveList.isEmpty()) { %>
                    <tr><td colspan="7" style="padding: 40px; color: #6c757d;">예약 내역이 없습니다.</td></tr>
                <% } else { for (ReservationDTO dto : reserveList) {
                    String displayStatus = dto.getStatus(); String statusClass = "bg-primary";
                    if ("예약완료".equals(displayStatus)) {
                        try {
                            LocalDateTime endDateTime = LocalDateTime.of(((java.sql.Date) dto.getResDate()).toLocalDate(), LocalTime.parse(dto.getEndTime()));
                            if (LocalDateTime.now().isAfter(endDateTime)) { displayStatus = "이용 종료"; statusClass = "bg-secondary"; }
                        } catch (Exception e) {}
                    } else if ("취소됨".equals(displayStatus)) { statusClass = "bg-danger"; }
                %>
                    <tr><td><%=dto.getResNo()%></td><td style="font-weight: 600;"><%=dto.getRoomId()%>호</td><td><%=dto.getResDate()%></td><td><%=dto.getStartTime()%> ~ <%=dto.getEndTime()%></td><td><%=dto.getPurpose()%></td><td><span class="status-badge <%=statusClass%>"><%=displayStatus%></span></td><td>
                    <% if ("예약완료".equals(displayStatus)) { %><button class="btn-action btn-cancel" onclick="cancelReserve(<%=dto.getResNo()%>)">예약 취소</button><% } else { %>-<% } %>
                    </td></tr>
                <% } } %>
                </tbody>
            </table>
        </div>

        <!-- 2. 비품 대여 현황 -->
        <div class="section-title">내 비품 대여 현황</div>
        <div class="table-wrapper">
            <table>
                <thead><tr><th>기안 번호</th><th>기안 제목</th><th>비품명</th><th>대여 기간</th><th>상태</th><th>비고</th></tr></thead>
                <tbody>
                <% if (myList == null || myList.isEmpty()) { %>
                    <tr><td colspan="6" style="padding: 40px; color: #6c757d;">대여 내역이 없습니다.</td></tr>
                <% } else { for (RentalHistoryDTO item : myList) {
                    String status = item.getStatus(); String badgeClass = "bg-secondary";
                    if ("승인대기".equals(status)) badgeClass = "bg-warning";
                    else if ("대여중".equals(status) || "미반납".equals(status)) badgeClass = "bg-success";
                    else if ("반려됨".equals(status)) badgeClass = "bg-danger";
                %>
                    <tr><td><%=item.getRentalNo()%></td><td style="text-align: left; padding-left: 20px;"><%=item.getTitle() != null ? item.getTitle() : "제목 없음"%></td><td style="font-weight: 600;"><%=item.getEqName()%></td><td><%=item.getRentalDate()%> ~ <%=item.getReturnDate()%></td><td><span class="status-badge <%=badgeClass%>"><%=status%></span></td><td>
                    <% if ("대여중".equals(status) || "미반납".equals(status)) { %><button class="btn-action btn-return" onclick="processReturn('<%=item.getRentalNo()%>')">반납 처리</button><% } else { %>-<% } %>
                    </td></tr>
                <% } } %>
                </tbody>
            </table>
        </div>

        <!-- 3. 휴가 신청 현황 -->
        <div class="section-title">내 휴가 신청 현황</div>
        <div class="table-wrapper">
            <table>
                <thead><tr><th>문서 번호</th><th>휴가 기간</th><th>사용 일수</th><th>사유</th><th>상태</th></tr></thead>
                <tbody>
                <% if (myLeaveList == null || myLeaveList.isEmpty()) { %>
                    <tr><td colspan="5" style="padding: 40px; color: #6c757d;">휴가 신청 내역이 없습니다.</td></tr>
                <% } else { for (LeaveHistoryDTO leave : myLeaveList) {
                    String badgeClass = "bg-warning";
                    if ("승인완료".equals(leave.getStatus())) badgeClass = "bg-success";
                    else if ("반려됨".equals(leave.getStatus())) badgeClass = "bg-danger";
                %>
                    <tr><td><%=leave.getLeaveNo()%></td><td><%=leave.getStartDate()%> ~ <%=leave.getEndDate()%></td><td><b><%=leave.getUseDays()%>일</b></td><td style="text-align: left; padding-left: 20px;"><%=leave.getReason()%></td><td><span class="status-badge <%=badgeClass%>"><%=leave.getStatus()%></span></td></tr>
                <% } } %>
                </tbody>
            </table>
        </div>
    </div>

</body>
</html>