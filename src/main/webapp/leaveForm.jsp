<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.office.dto.EmployeeDTO"%>
<%
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    if (loginEmp == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사내 시스템 - 휴가 신청</title>
<style>
    /* 1. 전체 배경 및 폰트 설정 */
    body { font-family: 'Segoe UI', 'Malgun Gothic', sans-serif; background-color: #f4f6f9; margin: 0; padding: 0; }
    
    /* 2. 상단 헤더 바 */
    .header { display: flex; justify-content: space-between; align-items: center; background-color: #212529; color: #ffffff; padding: 15px 30px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); margin-bottom: 40px; }
    .header h2 { margin: 0; font-size: 20px; letter-spacing: 1px; }
    .nav-buttons { display: flex; align-items: center; gap: 10px; }
    .nav-btn { padding: 8px 16px; background-color: #343a40; color: #ffffff; text-decoration: none; border-radius: 4px; font-weight: 600; font-size: 13px; border: 1px solid #495057; transition: all 0.2s; }
    .nav-btn:hover { background-color: #495057; border-color: #6c757d; }

    /* 3. 메인 컨테이너 - ★ 상단 테두리 색상 변경 (#28a745 -> #343a40) */
    .container { max-width: 600px; margin: 0 auto; background: white; padding: 40px; border-radius: 8px; box-shadow: 0 2px 15px rgba(0,0,0,0.05); border-top: 5px solid #343a40; }
    
    /* 4. 섹션 타이틀 */
    .section-title { font-size: 20px; font-weight: bold; margin: 0 0 25px 0; color: #343a40; display: flex; align-items: center; }
    .section-title::before { content: ''; display: inline-block; width: 4px; height: 20px; background-color: #6c757d; margin-right: 10px; }

    /* 5. 신청자 정보 박스 - ★ 연차 강조 색상 변경 (#28a745 -> #343a40) */
    .info-card { background: #f8f9fa; padding: 20px; margin-bottom: 30px; border-radius: 6px; border: 1px solid #e9ecef; display: flex; justify-content: space-between; align-items: center; }
    .info-card p { margin: 5px 0; font-size: 14px; color: #495057; }
    .leave-count { font-size: 18px; font-weight: bold; color: #343a40; }

    /* 6. 폼 요소 스타일 */
    .form-group { margin-bottom: 25px; }
    label { display: block; font-weight: 600; font-size: 14px; color: #343a40; margin-bottom: 8px; }
    input[type="date"], input[type="text"] { width: 100%; padding: 12px; border: 1px solid #ced4da; border-radius: 4px; box-sizing: border-box; font-size: 14px; transition: border-color 0.2s; }
    input:focus { outline: none; border-color: #6c757d; }

    /* 7. 결재 상신 버튼 */
    .btn-submit { width: 100%; padding: 15px; background: #343a40; color: white; border: none; border-radius: 4px; font-size: 15px; font-weight: bold; cursor: pointer; transition: background 0.2s; }
    .btn-submit:hover { background: #212529; }
    
    .footer-note { font-size: 12px; color: #adb5bd; margin-top: 15px; text-align: center; }
</style>
</head>
<body>

    <div class="header">
        <h2>Groupware</h2>
        <div class="nav-buttons">
            <span style="margin-right: 20px; font-size: 14px;"><b><%=loginEmp.getEmpName()%></b>님</span>
            <a href="main.jsp" class="nav-btn">시스템 메인으로</a>
            <a href="documentList.do" class="nav-btn">기안 문서함</a>
            <a href="logout.do" class="nav-btn" style="text-decoration: underline; background: none; border: none; color: #adb5bd; cursor: pointer;">로그아웃</a>
        </div>
    </div>

    <div class="container">
        <div class="section-title">휴가 신청서 작성</div>

        <div class="info-card">
            <div>
                <p><b>신청인:</b> <%=loginEmp.getEmpName()%> (<%=loginEmp.getDept()%>)</p>
                <p><b>직급:</b> <%=loginEmp.getEmpLevel()%>단계</p>
            </div>
            <div style="text-align: right;">
                <p>잔여 연차</p>
                <span class="leave-count"><%=loginEmp.getCurLeave()%> / <%=loginEmp.getMaxLeave()%></span>
            </div>
        </div>

        <form action="leaveProcess.do" method="post" onsubmit="return validate();">
            <div class="form-group">
                <label>휴가 시작일</label>
                <input type="date" id="startDate" name="startDate" required>
            </div>
            
            <div class="form-group">
                <label>휴가 종료일</label>
                <input type="date" id="endDate" name="endDate" required>
            </div>
            
            <div class="form-group">
                <label>휴가 사유</label>
                <input type="text" name="reason" placeholder="예: 개인 용무, 병원 진료 등" required>
            </div>

            <button type="submit" class="btn-submit">전자 결재 상신</button>
            <p class="footer-note">* 주말을 제외한 평일 기준 연차가 차감됩니다.</p>
        </form>
    </div>

    <script>
    function validate() {
        const start = new Date(document.getElementById('startDate').value);
        const end = new Date(document.getElementById('endDate').value);
        const curLeave = <%=loginEmp.getCurLeave()%>;

        if(start > end) { 
            alert('시작일이 종료일보다 늦을 수 없습니다.'); 
            return false; 
        }

        let days = 0;
        let cur = new Date(start);
        while(cur <= end) {
            if(cur.getDay() !== 0 && cur.getDay() !== 6) days++;
            cur.setDate(cur.getDate() + 1);
        }

        if(days === 0) {
            alert('신청하신 기간 내에 평일이 없습니다.');
            return false;
        }

        if(days > curLeave) {
            alert('보유하신 연차가 부족합니다.\n(신청 필요일수: ' + days + '일 / 잔여 연차: ' + curLeave + '일)');
            return false;
        }

        return confirm('총 ' + days + '일의 휴가를 신청하시겠습니까?\n(상신 후에는 취소가 불가능할 수 있습니다.)');
    }
    </script>
</body>
</html>