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
<title>사내 시스템 - 예약 신청</title>
<style>
    body { font-family: 'Segoe UI', 'Malgun Gothic', sans-serif; background-color: #f4f6f9; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
    .reserve-container { background-color: #ffffff; padding: 40px; border-radius: 8px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05); width: 480px; border-top: 5px solid #343a40; }
    .reserve-container h2 { text-align: left; color: #212529; margin-top: 0; margin-bottom: 20px; font-size: 20px; border-bottom: 2px solid #e9ecef; padding-bottom: 15px; }
    
    .info-box { background-color: #f8f9fa; padding: 15px; border-radius: 4px; margin-bottom: 25px; border: 1px solid #dee2e6; }
    .info-box h3 { margin: 0 0 8px 0; color: #343a40; font-size: 16px; }
    .info-box p { margin: 4px 0 0 0; font-size: 13px; color: #6c757d; }

    .form-group { margin-bottom: 25px; }
    .form-group label { display: block; margin-bottom: 8px; font-weight: 600; color: #495057; font-size: 13px; }
    .form-group input[type="date"], .form-group input[type="text"] { width: 100%; padding: 12px; border: 1px solid #ced4da; border-radius: 4px; box-sizing: border-box; background-color: #ffffff; }
    .form-group input:focus { border-color: #495057; outline: none; }
    
    .time-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 8px; margin-top: 10px; }
    .time-btn { padding: 10px 0; background-color: #ffffff; border: 1px solid #ced4da; border-radius: 4px; font-size: 14px; font-weight: 600; cursor: pointer; color: #495057; transition: all 0.15s; }
    .time-btn:hover:not(:disabled) { border-color: #343a40; color: #343a40; background-color: #f8f9fa; }
    .time-btn.active { background-color: #343a40; color: #ffffff; border-color: #343a40; }
    .time-btn:disabled { background-color: #e9ecef; color: #adb5bd; cursor: not-allowed; border-color: #e9ecef; }
    .time-btn.active:disabled { background-color: #6c757d; color: #ffffff; border-color: #6c757d; opacity: 1; }

    .timer-box { display: none; background-color: #fff3cd; border: 1px solid #ffeeba; padding: 12px; border-radius: 4px; text-align: center; margin-bottom: 20px; font-size: 13px; color: #856404; }
    .timer-text { font-weight: bold; color: #d32f2f; font-size: 16px; margin-left: 5px; }

    .btn-group { display: flex; gap: 10px; margin-top: 30px; }
    .btn-action { flex: 1; padding: 14px; border: none; border-radius: 4px; font-size: 14px; cursor: pointer; font-weight: bold; text-align: center; text-decoration: none; box-sizing: border-box; transition: 0.2s; }
    .btn-hold { background-color: #495057; color: white; width: 100%; margin-top: 15px; }
    .btn-hold:hover { background-color: #343a40; }
    .btn-submit { background-color: #212529; color: white; }
    .btn-submit:hover:not(:disabled) { background-color: #000000; }
    .btn-submit:disabled { background-color: #adb5bd; cursor: not-allowed; }
    .btn-cancel { background-color: #ffffff; color: #495057; border: 1px solid #ced4da; }
    .btn-cancel:hover { background-color: #f8f9fa; }
</style>

<script>
    let timerInterval = null; 
    let selectedTimes = []; 

    function isContinuous(times) {
        if (times.length <= 1) return true;
        for (let i = 0; i < times.length - 1; i++) {
            let t1 = parseInt(times[i].split(':')[0]);
            let t2 = parseInt(times[i+1].split(':')[0]);
            if (t2 - t1 !== 1) return false;
        }
        return true;
    }

    function startTimer(startStr, endStr) {
        let timeLeft = 300; // 5분 (테스트용 10초로 변경 가능)
        const timerBox = document.getElementById("timerBox");
        const timeDisplay = document.getElementById("timeDisplay");
        const submitBtn = document.getElementById("submitBtn");
        const holdBtn = document.getElementById("holdBtn");
        const holdTimeText = document.getElementById("holdTimeText");

        holdTimeText.innerText = "[" + startStr + " ~ " + endStr + "]";

        timerBox.style.display = "block"; 
        submitBtn.disabled = false; 
        holdBtn.style.display = "none"; 

        document.querySelectorAll('.time-btn').forEach(btn => btn.disabled = true);

        if(timerInterval) clearInterval(timerInterval); 

        timerInterval = setInterval(function() {
            timeLeft--;
            let minutes = Math.floor(timeLeft / 60);
            let seconds = timeLeft % 60;
            timeDisplay.textContent = (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;

            if (timeLeft <= 0) {
                clearInterval(timerInterval);
                alert("예약 대기 시간이 만료되었습니다. 다시 진행해 주세요.");
                location.reload(); 
            }
        }, 1000);
    }

    document.addEventListener("DOMContentLoaded", function() {
        const timeButtons = document.querySelectorAll('.time-btn');
        const resDateInput = document.getElementById("resDate");
        const holdBtn = document.getElementById("holdBtn");
        
        // ★ 오늘 날짜 구해서 달력(min)에 세팅하기 (과거 날짜 원천 차단)
        const today = new Date();
        const todayStr = today.getFullYear() + '-' + String(today.getMonth() + 1).padStart(2, '0') + '-' + String(today.getDate()).padStart(2, '0');
        resDateInput.setAttribute("min", todayStr);
        
        timeButtons.forEach(btn => {
            btn.addEventListener('click', function() {
                if(this.disabled) return;
                const timeStr = this.getAttribute('data-time');

                if (this.classList.contains('active')) {
                    this.classList.remove('active');
                    selectedTimes = selectedTimes.filter(t => t !== timeStr);
                    if (!isContinuous(selectedTimes)) {
                        alert("연속된 시간만 선택 가능합니다.");
                        this.classList.add('active');
                        selectedTimes.push(timeStr);
                        selectedTimes.sort();
                    }
                } else {
                    selectedTimes.push(timeStr);
                    selectedTimes.sort();
                    if (!isContinuous(selectedTimes)) {
                        alert("연속된 시간만 선택 가능합니다.");
                        selectedTimes = selectedTimes.filter(t => t !== timeStr);
                    } else {
                        this.classList.add('active');
                    }
                }
            });
        });

        holdBtn.addEventListener('click', function() {
            const selectedDate = resDateInput.value;
            if(!selectedDate) { alert("예약 일자를 먼저 선택해 주세요."); return; }
            if(selectedTimes.length === 0) { alert("예약할 시간을 선택해 주세요."); return; }

            const startTime = selectedTimes[0];
            let lastHour = parseInt(selectedTimes[selectedTimes.length - 1].split(':')[0]);
            let endTimeStr = (lastHour + 1 < 10 ? '0' + (lastHour + 1) : (lastHour + 1)) + ':00';
            
            const roomId = document.getElementById("roomId").value;
            const empNo = document.getElementById("empNo").value;

            const formData = new URLSearchParams();
            formData.append("roomId", roomId);
            formData.append("empNo", empNo);
            formData.append("resDate", selectedDate);
            formData.append("startTime", startTime);
            formData.append("endTime", endTimeStr); 

            fetch('holdReserve.do', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: formData.toString()
            })
            .then(response => response.json())
            .then(data => {
                if(data.result === 'success') {
                    document.getElementById('resNo').value = data.resNo;
                    startTimer(startTime, endTimeStr);
                } else {
                    alert("선택하신 시간에 이미 등록된 예약이 존재합니다. 새로고침 후 다시 시도해 주세요.");
                    location.reload();
                }
            })
            .catch(error => console.error('Error:', error));
        });

        resDateInput.addEventListener('change', function() {
            const selectedDate = this.value;
            if (!selectedDate) return;
            
            // 날짜 수동 입력 변경 시 과거 날짜인지 한 번 더 검증
            if (selectedDate < todayStr) {
                alert("과거 날짜는 예약할 수 없습니다.");
                this.value = "";
                return;
            }
            
            const roomId = document.getElementById("roomId").value;

            fetch('checkReservedTime.do?roomId=' + roomId + '&resDate=' + selectedDate)
                .then(response => response.json())
                .then(reservedTimes => {
                    selectedTimes = []; 
                    timeButtons.forEach(btn => { btn.disabled = false; btn.classList.remove('active'); });
                    
                    clearInterval(timerInterval);
                    document.getElementById("timerBox").style.display = "none";
                    document.getElementById("submitBtn").disabled = true;
                    document.getElementById("holdBtn").style.display = "block";
                    document.getElementById("resNo").value = "";

                    // ★ 추가: 현재 시간 구하기 (오늘 날짜를 선택했을 때만 비교하기 위해)
                    const now = new Date();
                    const isToday = (selectedDate === todayStr); // 앞서 구한 todayStr과 비교
                    const currentHour = now.getHours();

                    timeButtons.forEach(btn => {
                        const btnTime = btn.getAttribute('data-time');
                        const btnHour = parseInt(btnTime.split(':')[0]);

                        // 1. 이미 다른 사람이 예약한 시간 비활성화
                        if (reservedTimes.includes(btnTime)) {
                            btn.disabled = true;
                        }
                        // 2. ★ 추가: 오늘 날짜이면서, 현재 시간보다 과거인 버튼 비활성화
                        else if (isToday && btnHour <= currentHour) {
                            btn.disabled = true;
                        }
                    });
                })
                .catch(error => console.error('Error:', error));
        });
    });

    function validateForm() {
        if (!document.getElementById("resNo").value) { alert("예약 시간을 먼저 지정해 주세요."); return false; }
        if (!document.getElementById("purpose").value.trim()) { alert("사용 목적을 기재해 주세요."); return false; }
        return true; 
    }
</script>
</head>
<body>

<div class="reserve-container">
    <h2>회의실 예약 신청</h2>
    
    <div class="info-box">
        <h3>[<%= roomInfo.getRoomId() %>호] <%= roomInfo.getRoomName() %></h3>
        <p><b>수용 인원:</b> 최대 <%= roomInfo.getCapacity() %>명 &nbsp;|&nbsp; <b>프로젝터:</b> <%= "Y".equals(roomInfo.getHasBeam()) ? "보유" : "미보유" %></p>
        <p style="margin-top: 8px;"><%= roomInfo.getDescription() %></p>
    </div>
    
    <div class="timer-box" id="timerBox">
        <span id="holdTimeText" style="color: #495057; font-size: 14px; font-weight: bold;"></span><br>
        해당 시간대 확보 완료. 남은 시간 내에 확정해 주세요. <span class="timer-text" id="timeDisplay">05:00</span>
    </div>

    <form action="reserveProcess.do" method="post" onsubmit="return validateForm();">
        <input type="hidden" id="resNo" name="resNo" value="">
        <input type="hidden" id="roomId" name="roomId" value="<%= roomInfo.getRoomId() %>">
        <input type="hidden" id="empNo" name="empNo" value="<%= loginEmp.getEmpNo() %>">
        
        <div class="form-group">
            <label>예약 일자</label>
            <input type="date" id="resDate" name="resDate" required>
        </div>
        
        <div class="form-group">
            <label>시간 지정 (연속 선택 가능)</label>
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
                <button type="button" class="time-btn" data-time="18:00">18:00</button>
            </div>
            <button type="button" class="btn-action btn-hold" id="holdBtn">선택 시간 확보</button>
        </div>
        
        <div class="form-group">
            <label>사용 목적</label>
            <input type="text" id="purpose" name="purpose" placeholder="예: 주간 부서 회의" required>
        </div>
        
        <div class="btn-group">
            <a href="main.jsp" class="btn-action btn-cancel">취소</a>
            <button type="submit" class="btn-action btn-submit" id="submitBtn" disabled>예약 확정</button>
        </div>
    </form>
</div>

</body>
</html>