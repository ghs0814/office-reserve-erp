<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.office.dto.EmployeeDTO" %>
<%@ page import="com.office.dto.RoomDTO" %>
<%
    EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
    if (loginEmp == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    RoomDTO roomInfo = (RoomDTO) request.getAttribute("roomInfo");
    
    if (roomInfo == null) {
        response.sendRedirect("main.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오피스 예약 시스템 - 예약 신청</title>
<style>
    body {
        font-family: 'Malgun Gothic', sans-serif;
        background-color: #f0f2f5;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
    }
    
    .reserve-container {
        background-color: white;
        padding: 40px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        width: 450px;
    }
    
    .reserve-container h2 {
        text-align: center;
        color: #333;
        margin-bottom: 10px;
    }
    
    .room-badge {
        display: block;
        text-align: center;
        color: #4CAF50;
        font-weight: bold;
        font-size: 20px;
        margin-bottom: 30px;
    }
    
    .form-group {
        margin-bottom: 20px;
    }
    
    .form-group label {
        display: block;
        margin-bottom: 8px;
        font-weight: bold;
        color: #555;
        font-size: 14px;
    }
    
    .form-group input[type="date"],
    .form-group input[type="text"] {
        width: 100%;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 4px;
        box-sizing: border-box;
    }
    
    /* 시간 선택 버튼 그리드 스타일 */
    .time-grid {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: 10px;
        margin-top: 10px;
    }

    .time-btn {
        padding: 12px 0;
        background-color: white;
        border: 1px solid #ddd;
        border-radius: 6px;
        font-size: 15px;
        font-weight: bold;
        cursor: pointer;
        transition: all 0.2s;
        color: #333;
    }

    .time-btn:hover:not(:disabled) {
        border-color: #ff5722;
        color: #ff5722;
    }

    /* 선택되었을 때의 스타일 */
    .time-btn.active {
        background-color: #ff5722;
        color: white;
        border-color: #ff5722;
    }

    /* 예약 마감(비활성화) 스타일 - 추후 AJAX 연동 시 사용됨 */
    .time-btn:disabled {
        background-color: #f5f5f5;
        color: #ccc;
        cursor: not-allowed;
        border-color: #eee;
    }

    .time-info-text {
        font-size: 12px;
        color: #888;
        margin-top: 5px;
        text-align: right;
    }

    .btn-group {
        display: flex;
        gap: 10px;
        margin-top: 30px;
    }
    
    .btn-submit {
        flex: 1;
        padding: 12px;
        background-color: #4CAF50;
        color: white;
        border: none;
        border-radius: 4px;
        font-size: 16px;
        cursor: pointer;
        font-weight: bold;
    }
    
    .btn-cancel {
        flex: 1;
        padding: 12px;
        background-color: #ccc;
        color: #333;
        border: none;
        border-radius: 4px;
        font-size: 16px;
        cursor: pointer;
        text-align: center;
        text-decoration: none;
        font-weight: bold;
        box-sizing: border-box;
    }
    
    .btn-submit:hover { background-color: #45a049; }
    .btn-cancel:hover { background-color: #bbb; }
</style>

<script>
    // 페이지 로드 시 시간 버튼 이벤트 등록
    document.addEventListener("DOMContentLoaded", function() {
        const timeButtons = document.querySelectorAll('.time-btn');
        
        timeButtons.forEach(btn => {
            btn.addEventListener('click', function() {
                // 비활성화된 버튼은 클릭 방지
                if(this.disabled) return;

                // 1. 모든 버튼의 활성화 상태 해제
                timeButtons.forEach(b => b.classList.remove('active'));
                
                // 2. 클릭한 버튼 활성화
                this.classList.add('active');
                
                // 3. 숨겨진 input에 시작 시간 세팅
                const selectedTime = this.getAttribute('data-time');
                document.getElementById('startTime').value = selectedTime;
                
                // 4. 종료 시간 자동 계산 (1시간 뒤)
                let hour = parseInt(selectedTime.split(':')[0]);
                let endHour = hour + 1;
                let endTimeStr = (endHour < 10 ? '0' + endHour : endHour) + ':00';
                document.getElementById('endTime').value = endTimeStr;
            });
        });
    });

    function validateForm() {
        const resDateInput = document.getElementById("resDate").value;
        const startTimeInput = document.getElementById("startTime").value;
        const purposeInput = document.getElementById("purpose").value;

        // 1. 날짜 입력 확인
        if (!resDateInput) {
            alert("예약 날짜를 선택해주세요.");
            return false;
        }

        // 2. 과거 날짜 선택 방지
        const selectedDate = new Date(resDateInput);
        const today = new Date();
        today.setHours(0, 0, 0, 0); 

        if (selectedDate < today) {
            alert("과거의 날짜는 예약할 수 없습니다.");
            return false; 
        }

        // 3. 시간 선택 확인
        if (!startTimeInput) {
            alert("예약하실 시간을 선택해주세요.");
            return false;
        }

        // 4. 목적 입력 확인
        if (!purposeInput.trim()) {
            alert("사용 목적을 입력해주세요.");
            return false;
        }

        return true; 
    }
    
    document.addEventListener("DOMContentLoaded", function() {
        const resDateInput = document.getElementById("resDate");
        const timeButtons = document.querySelectorAll('.time-btn');
        const roomId = "<%= roomInfo.getRoomId() %>";

        // 날짜 변경 이벤트 리스너
        resDateInput.addEventListener('change', function() {
            const selectedDate = this.value;
            if (!selectedDate) return;

            // AJAX 요청 (fetch API 사용)
            fetch('checkReservedTime.do?roomId=' + roomId + '&resDate=' + selectedDate)
                .then(response => response.json())
                .then(reservedTimes => {
                    // 1. 모든 버튼 초기화 (활성화)
                    timeButtons.forEach(btn => {
                        btn.disabled = false;
                        btn.classList.remove('active'); // 날짜 바뀌면 선택했던 것도 해제
                    });
                    document.getElementById('startTime').value = "";
                    document.getElementById('endTime').value = "";

                    // 2. 서버에서 받아온 예약된 시간과 일치하는 버튼 비활성화
                    timeButtons.forEach(btn => {
                        if (reservedTimes.includes(btn.getAttribute('data-time'))) {
                            btn.disabled = true;
                        }
                    });
                })
                .catch(error => console.error('Error:', error));
        });

        // (기존 버튼 클릭 이벤트 로직은 그대로 유지)
    });
</script>

</head>
<body>

<div class="reserve-container">
    <h2>회의실 예약 신청</h2>
    
    <div style="background-color: #e3f2fd; padding: 15px; border-radius: 8px; margin-bottom: 20px; text-align: center;">
        <h3 style="margin: 0 0 10px 0; color: #1976d2;">
            [ <%= roomInfo.getRoomId() %>호 ] <%= roomInfo.getRoomName() %>
        </h3>
        <p style="margin: 5px 0; font-size: 14px; color: #555;">
            <b>수용 인원:</b> 최대 <%= roomInfo.getCapacity() %>명 | 
            <b>빔 프로젝터:</b> <%= "Y".equals(roomInfo.getHasBeam()) ? "있음 O" : "없음 X" %>
        </p>
        <p style="margin: 5px 0 0 0; font-size: 13px; color: #666;">
            <%= roomInfo.getDescription() %>
        </p>
    </div>
    
    <form action="reserveProcess.do" method="post" onsubmit="return validateForm();">
        
        <input type="hidden" name="roomId" value="<%= roomInfo.getRoomId() %>">
        <input type="hidden" name="empNo" value="<%= loginEmp.getEmpNo() %>">
        <input type="hidden" name="status" value="예약완료">
        
        <!-- 자바스크립트로 값이 채워질 숨김 필드 -->
        <input type="hidden" id="startTime" name="startTime">
        <input type="hidden" id="endTime" name="endTime">
        
        <div class="form-group">
            <label for="resDate">예약 날짜</label>
            <input type="date" id="resDate" name="resDate" required>
        </div>
        
        <div class="form-group">
            <label>시간 선택</label>
            <div class="time-grid" id="timeGrid">
                <!-- data-time 속성에 실제 들어갈 값을 지정합니다 -->
                <button type="button" class="time-btn" data-time="09:00">09:00</button>
                <button type="button" class="time-btn" data-time="10:00">10:00</button>
                <button type="button" class="time-btn" data-time="11:00">11:00</button>
                <button type="button" class="time-btn" data-time="12:00">12:00</button>
                <button type="button" class="time-btn" data-time="13:00">13:00</button>
                <button type="button" class="time-btn" data-time="14:00">14:00</button>
                <button type="button" class="time-btn" data-time="15:00">15:00</button>
                <button type="button" class="time-btn" data-time="16:00">16:00</button>
                <button type="button" class="time-btn" data-time="17:00">17:00</button>
                <button type="button" class="time-btn" data-time="18:00">18:00</button>
            </div>
            <div class="time-info-text">* 선택한 시간으로부터 1시간 동안 예약됩니다.</div>
        </div>
        
        <div class="form-group">
            <label for="purpose">사용 목적</label>
            <input type="text" id="purpose" name="purpose" placeholder="예) 주간 회의, 클라이언트 미팅 등" required>
        </div>
        
        <div class="btn-group">
            <a href="main.jsp" class="btn-cancel">취소</a>
            <button type="submit" class="btn-submit">예약하기</button>
        </div>
    </form>
</div>

</body>
</html>