<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.office.dto.EmployeeDTO" %>
<%@ page import="com.office.dto.RoomDTO" %>
<%
    // 1. 보안 체크 및 예약에 필요한 방 정보를 request에서 가져옵니다.
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
    /* 화면 레이아웃 및 디자인 설정 */
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
    
    /* 시간 선택 버튼 그리드 스타일 (4열 배치) */
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

    /* 선택된 시간 버튼 스타일 */
    .time-btn.active {
        background-color: #ff5722;
        color: white;
        border-color: #ff5722;
    }

    /* 이미 예약된 시간(비활성화) 버튼 스타일 */
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
    // 2. 시간 선택 버튼 이벤트 및 자동 시간 계산 로직
    document.addEventListener("DOMContentLoaded", function() {
        const timeButtons = document.querySelectorAll('.time-btn');
        
        timeButtons.forEach(btn => {
            btn.addEventListener('click', function() {
                if(this.disabled) return;

                // 모든 버튼 비활성화 해제 후 현재 버튼 활성화
                timeButtons.forEach(b => b.classList.remove('active'));
                this.classList.add('active');
                
                // 선택한 시작 시간과 자동으로 계산된 종료 시간(1시간 뒤)을 hidden input에 세팅
                const selectedTime = this.getAttribute('data-time');
                document.getElementById('startTime').value = selectedTime;
                
                let hour = parseInt(selectedTime.split(':')[0]);
                let endHour = hour + 1;
                let endTimeStr = (endHour < 10 ? '0' + endHour : endHour) + ':00';
                document.getElementById('endTime').value = endTimeStr;
            });
        });
    });

    // 3. 입력 데이터 유효성 검사 (과거 날짜 예약 방지 등)
    function validateForm() {
        const resDateInput = document.getElementById("resDate").value;
        const startTimeInput = document.getElementById("startTime").value;
        const purposeInput = document.getElementById("purpose").value;

        if (!resDateInput) {
            alert("예약 날짜를 선택해주세요.");
            return false;
        }

        const selectedDate = new Date(resDateInput);
        const today = new Date();
        today.setHours(0, 0, 0, 0); 

        if (selectedDate < today) {
            alert("과거의 날짜는 예약할 수 없습니다.");
            return false; 
        }

        if (!startTimeInput) {
            alert("예약하실 시간을 선택해주세요.");
            return false;
        }

        if (!purposeInput.trim()) {
            alert("사용 목적을 입력해주세요.");
            return false;
        }

        return true; 
    }
    
    // 4. [AJAX] 날짜 변경 시 해당 일자의 이미 예약된 시간 목록을 가져와서 버튼을 비활성화함
    document.addEventListener("DOMContentLoaded", function() {
        const resDateInput = document.getElementById("resDate");
        const timeButtons = document.querySelectorAll('.time-btn');
        const roomId = "<%= roomInfo.getRoomId() %>";

        resDateInput.addEventListener('change', function() {
            const selectedDate = this.value;
            if (!selectedDate) return;

            // Fetch API를 사용하여 비동기적으로 중복 예약 시간 조회
            fetch('checkReservedTime.do?roomId=' + roomId + '&resDate=' + selectedDate)
                .then(response => response.json())
                .then(reservedTimes => {
                    // 모든 버튼 활성화 초기화 및 선택 값 제거
                    timeButtons.forEach(btn => {
                        btn.disabled = false;
                        btn.classList.remove('active');
                    });
                    document.getElementById('startTime').value = "";
                    document.getElementById('endTime').value = "";

                    // 조회된 데이터에 포함된 시간 버튼들만 비활성화 처리
                    timeButtons.forEach(btn => {
                        if (reservedTimes.includes(btn.getAttribute('data-time'))) {
                            btn.disabled = true;
                        }
                    });
                })
                .catch(error => console.error('Error:', error));
        });
    });
</script>

</head>
<body>

<div class="reserve-container">
    <h2>회의실 예약 신청</h2>
    
    <!-- 5. 선택한 회의실의 수용 인원 및 장비 정보를 상단 박스에 표시 -->
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
    
    <!-- 6. 예약 처리를 위한 폼 구성 (ReserveProcessController로 연결) -->
    <form action="reserveProcess.do" method="post" onsubmit="return validateForm();">
        
        <input type="hidden" name="roomId" value="<%= roomInfo.getRoomId() %>">
        <input type="hidden" name="empNo" value="<%= loginEmp.getEmpNo() %>">
        <input type="hidden" name="status" value="예약완료">
        
        <!-- 버튼 클릭 시 자바스크립트로 세팅될 숨김 데이터 -->
        <input type="hidden" id="startTime" name="startTime">
        <input type="hidden" id="endTime" name="endTime">
        
        <div class="form-group">
            <label for="resDate">예약 날짜</label>
            <input type="date" id="resDate" name="resDate" required>
        </div>
        
        <div class="form-group">
            <label>시간 선택</label>
            <div class="time-grid" id="timeGrid">
                <!-- data-time 속성의 값을 기준으로 예약 중복 여부를 체크합니다 -->
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