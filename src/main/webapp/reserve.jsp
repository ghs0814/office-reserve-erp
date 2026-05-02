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
    body { font-family: 'Malgun Gothic', sans-serif; background-color: #f0f2f5; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
    .reserve-container { background-color: white; padding: 40px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); width: 450px; }
    .reserve-container h2 { text-align: center; color: #333; margin-bottom: 10px; }
    
    .form-group { margin-bottom: 20px; }
    .form-group label { display: block; margin-bottom: 8px; font-weight: bold; color: #555; font-size: 14px; }
    .form-group input[type="date"], .form-group input[type="text"] { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
    
    .time-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; margin-top: 10px; }
    .time-btn { padding: 12px 0; background-color: white; border: 1px solid #ddd; border-radius: 6px; font-size: 15px; font-weight: bold; cursor: pointer; transition: all 0.2s; color: #333; }
    .time-btn:hover:not(:disabled) { border-color: #ff5722; color: #ff5722; }
    .time-btn.active { background-color: #ff5722; color: white; border-color: #ff5722; }
    .time-btn:disabled { background-color: #f5f5f5; color: #ccc; cursor: not-allowed; border-color: #eee; }

    /* 타이머 UI 디자인 */
    .timer-box { display: none; background-color: #fff3cd; border: 1px solid #ffeeba; color: #856404; padding: 10px; border-radius: 4px; text-align: center; font-weight: bold; margin-bottom: 20px; font-size: 14px; }
    .timer-text { color: #d9534f; font-size: 18px; margin-left: 5px; }

    .btn-group { display: flex; gap: 10px; margin-top: 30px; }
    .btn-submit { flex: 1; padding: 12px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; font-size: 16px; cursor: pointer; font-weight: bold; }
    .btn-cancel { flex: 1; padding: 12px; background-color: #ccc; color: #333; border: none; border-radius: 4px; font-size: 16px; cursor: pointer; text-align: center; text-decoration: none; font-weight: bold; box-sizing: border-box; }
    .btn-submit:hover { background-color: #45a049; }
    .btn-cancel:hover { background-color: #bbb; }
</style>

<script>
    let timerInterval = null; // 타이머 변수

    // 1. 타이머 작동 함수 (5분)
    function startTimer() {
        let timeLeft = 300; // 5분 = 300초
        //let timeLeft = 10; // 테스트용: 10초
        const timerBox = document.getElementById("timerBox");
        const timeDisplay = document.getElementById("timeDisplay");
        const submitBtn = document.getElementById("submitBtn");

        timerBox.style.display = "block"; // 타이머 UI 보이기
        submitBtn.disabled = false; // 예약하기 버튼 활성화

        if(timerInterval) clearInterval(timerInterval); // 기존 타이머 리셋

        timerInterval = setInterval(function() {
            timeLeft--;
            let minutes = Math.floor(timeLeft / 60);
            let seconds = timeLeft % 60;
            timeDisplay.textContent = (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;

            if (timeLeft <= 0) {
                clearInterval(timerInterval);
                alert("선점 시간이 만료되었습니다. 다시 시간을 선택해주세요.");
                location.reload(); // 페이지 새로고침
            }
        }, 1000);
    }

    // 2. 시간 선택 시 AJAX 선점 로직
    document.addEventListener("DOMContentLoaded", function() {
        const timeButtons = document.querySelectorAll('.time-btn');
        const resDateInput = document.getElementById("resDate");
        
        timeButtons.forEach(btn => {
            btn.addEventListener('click', function() {
                if(this.disabled) return;
                
                const selectedDate = resDateInput.value;
                if(!selectedDate) {
                    alert("예약 날짜를 먼저 선택해주세요.");
                    return;
                }

                const selectedTime = this.getAttribute('data-time');
                let hour = parseInt(selectedTime.split(':')[0]);
                let endTimeStr = (hour + 1 < 10 ? '0' + (hour + 1) : (hour + 1)) + ':00';
                
                const roomId = document.getElementById("roomId").value;
                const empNo = document.getElementById("empNo").value;

                // 폼 데이터 생성
                const formData = new URLSearchParams();
                formData.append("roomId", roomId);
                formData.append("empNo", empNo);
                formData.append("resDate", selectedDate);
                formData.append("startTime", selectedTime);
                formData.append("endTime", endTimeStr);

                // AJAX 비동기 요청 (holdReserve.do)
                fetch('holdReserve.do', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: formData.toString()
                })
                .then(response => response.json())
                .then(data => {
                    if(data.result === 'success') {
                        // 선점 성공: 버튼 UI 변경
                        timeButtons.forEach(b => b.classList.remove('active'));
                        this.classList.add('active');
                        
                        // 발급받은 임시 예약번호를 hidden 영역에 저장
                        document.getElementById('resNo').value = data.resNo;
                        
                        // 5분 타이머 시작
                        startTimer();
                    } else {
                        alert("다른 분이 방금 선점한 시간입니다. 다른 시간을 선택해주세요.");
                        // 비활성화 처리
                        this.disabled = true;
                    }
                })
                .catch(error => console.error('Error:', error));
            });
        });

        // 3. 날짜 변경 시 예약된 시간 불러오기 (기존 로직 유지)
        resDateInput.addEventListener('change', function() {
            const selectedDate = this.value;
            if (!selectedDate) return;

            const roomId = document.getElementById("roomId").value;

            fetch('checkReservedTime.do?roomId=' + roomId + '&resDate=' + selectedDate)
                .then(response => response.json())
                .then(reservedTimes => {
                    timeButtons.forEach(btn => {
                        btn.disabled = false;
                        btn.classList.remove('active');
                    });
                    
                    // 날짜를 바꾸면 타이머와 임시번호 리셋
                    clearInterval(timerInterval);
                    document.getElementById("timerBox").style.display = "none";
                    document.getElementById("submitBtn").disabled = true;
                    document.getElementById("resNo").value = "";

                    timeButtons.forEach(btn => {
                        if (reservedTimes.includes(btn.getAttribute('data-time'))) {
                            btn.disabled = true;
                        }
                    });
                })
                .catch(error => console.error('Error:', error));
        });
    });

    function validateForm() {
        if (!document.getElementById("resNo").value) {
            alert("시간을 선택하여 예약을 선점해주세요.");
            return false;
        }
        if (!document.getElementById("purpose").value.trim()) {
            alert("사용 목적을 입력해주세요.");
            return false;
        }
        return true; 
    }
</script>
</head>
<body>

<div class="reserve-container">
    <h2>회의실 예약 신청</h2>
    
    <div style="background-color: #e3f2fd; padding: 15px; border-radius: 8px; margin-bottom: 20px; text-align: center;">
        <h3 style="margin: 0 0 10px 0; color: #1976d2;">[ <%= roomInfo.getRoomId() %>호 ] <%= roomInfo.getRoomName() %></h3>
        <p style="margin: 5px 0; font-size: 14px; color: #555;">
            <b>수용 인원:</b> 최대 <%= roomInfo.getCapacity() %>명 | 
            <b>빔 프로젝터:</b> <%= "Y".equals(roomInfo.getHasBeam()) ? "있음 O" : "없음 X" %>
        </p>
        <p style="margin: 5px 0 0 0; font-size: 13px; color: #666;"><%= roomInfo.getDescription() %></p>
    </div>
    
    <!-- ★ 새로 추가된 타이머 영역 -->
    <div class="timer-box" id="timerBox">
        임시 선점 완료! 남은 시간 내에 확정해주세요 <span class="timer-text" id="timeDisplay">05:00</span>
    </div>

    <form action="reserveProcess.do" method="post" onsubmit="return validateForm();">
        <!-- 이제 AJAX로 발급받은 resNo만 넘기면 됩니다 -->
        <input type="hidden" id="resNo" name="resNo" value="">
        <input type="hidden" id="roomId" name="roomId" value="<%= roomInfo.getRoomId() %>">
        <input type="hidden" id="empNo" name="empNo" value="<%= loginEmp.getEmpNo() %>">
        
        <div class="form-group">
            <label for="resDate">예약 날짜</label>
            <input type="date" id="resDate" name="resDate" required>
        </div>
        
        <div class="form-group">
            <label>시간 선택</label>
            <div class="time-grid" id="timeGrid">
                <button type="button" class="time-btn" data-time="09:00">09:00</button>
                <button type="button" class="time-btn" data-time="10:00">10:00</button>
                <button type="button" class="time-btn" data-time="11:00">11:00</button>
                <button type="button" class="time-btn" data-time="12:00">12:00</button>
                <button type="button" class="time-btn" data-time="13:00">13:00</button>
                <button type="button" class="time-btn" data-time="14:00">14:00</button>
                <button type="button" class="time-btn" data-time="15:00">15:00</button>
                <button type="button" class="time-btn" data-time="16:00">16:00</button>
                <button type="button" class="time-btn" data-time="17:00">17:00</button>
                <!-- <button type="button" class="time-btn" data-time="18:00">18:00</button> -->
            </div>
        </div>
        
        <div class="form-group">
            <label for="purpose">사용 목적</label>
            <input type="text" id="purpose" name="purpose" placeholder="예) 주간 회의, 클라이언트 미팅 등" required>
        </div>
        
        <div class="btn-group">
            <a href="main.jsp" class="btn-cancel">취소</a>
            <button type="submit" class="btn-submit" id="submitBtn" disabled>예약 확정하기</button>
        </div>
    </form>
</div>

</body>
</html>