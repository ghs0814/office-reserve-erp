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
    
    /* ★ 추가됨: 선택된 상태에서 비활성화된 버튼은 연한 주황색으로 유지 */
    .time-btn.active:disabled { background-color: #ff8a65; color: white; border-color: #ff8a65; opacity: 0.9; }

    .timer-box { display: none; background-color: #fff3cd; border: 1px solid #ffeeba; color: #856404; padding: 10px; border-radius: 4px; text-align: center; font-weight: bold; margin-bottom: 20px; font-size: 14px; }
    .timer-text { color: #d9534f; font-size: 18px; margin-left: 5px; }

    .btn-group { display: flex; gap: 10px; margin-top: 30px; }
    .btn-action { flex: 1; padding: 12px; color: white; border: none; border-radius: 4px; font-size: 16px; cursor: pointer; font-weight: bold; text-align: center; text-decoration: none; box-sizing: border-box; }
    .btn-hold { background-color: #2196F3; width: 100%; margin-top: 10px; }
    .btn-hold:hover { background-color: #1976d2; }
    .btn-submit { background-color: #4CAF50; }
    .btn-submit:hover:not(:disabled) { background-color: #45a049; }
    .btn-submit:disabled { background-color: #a5d6a7; cursor: not-allowed; }
    .btn-cancel { background-color: #ccc; color: #333; }
    .btn-cancel:hover { background-color: #bbb; }
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

    // ★ 수정됨: 시작 시간과 종료 시간을 받아서 텍스트로 띄워줍니다.
    function startTimer(startStr, endStr) {
        let timeLeft = 300; // 5분
        const timerBox = document.getElementById("timerBox");
        const timeDisplay = document.getElementById("timeDisplay");
        const submitBtn = document.getElementById("submitBtn");
        const holdBtn = document.getElementById("holdBtn");
        const holdTimeText = document.getElementById("holdTimeText");

        // 안내 문구에 선택한 시간 범위 표시
        holdTimeText.innerText = "[" + startStr + " ~ " + endStr + "]";

        timerBox.style.display = "block"; 
        submitBtn.disabled = false; 
        holdBtn.style.display = "none"; 

        // 시간 버튼 모두 비활성화 (선점 후 수정 불가) -> CSS 적용으로 선택된 버튼은 주황색 유지
        document.querySelectorAll('.time-btn').forEach(btn => btn.disabled = true);

        if(timerInterval) clearInterval(timerInterval); 

        timerInterval = setInterval(function() {
            timeLeft--;
            let minutes = Math.floor(timeLeft / 60);
            let seconds = timeLeft % 60;
            timeDisplay.textContent = (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;

            if (timeLeft <= 0) {
                clearInterval(timerInterval);
                alert("선점 시간이 만료되었습니다. 다시 시간을 선택해주세요.");
                location.reload(); 
            }
        }, 1000);
    }

    document.addEventListener("DOMContentLoaded", function() {
        const timeButtons = document.querySelectorAll('.time-btn');
        const resDateInput = document.getElementById("resDate");
        const holdBtn = document.getElementById("holdBtn");
        
        timeButtons.forEach(btn => {
            btn.addEventListener('click', function() {
                if(this.disabled) return;
                
                const timeStr = this.getAttribute('data-time');

                if (this.classList.contains('active')) {
                    this.classList.remove('active');
                    selectedTimes = selectedTimes.filter(t => t !== timeStr);
                    
                    if (!isContinuous(selectedTimes)) {
                        alert("연속된 시간만 선택 가능합니다. 중간 시간을 뺄 수 없습니다.");
                        this.classList.add('active');
                        selectedTimes.push(timeStr);
                        selectedTimes.sort();
                    }
                } else {
                    selectedTimes.push(timeStr);
                    selectedTimes.sort();
                    
                    if (!isContinuous(selectedTimes)) {
                        alert("연속된 시간만 선택 가능합니다. 빈 시간을 먼저 채워주세요.");
                        selectedTimes = selectedTimes.filter(t => t !== timeStr);
                    } else {
                        this.classList.add('active');
                    }
                }
            });
        });

        holdBtn.addEventListener('click', function() {
            const selectedDate = resDateInput.value;
            if(!selectedDate) {
                alert("예약 날짜를 먼저 선택해주세요.");
                return;
            }
            if(selectedTimes.length === 0) {
                alert("예약할 시간을 하나 이상 선택해주세요.");
                return;
            }

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
                    // ★ 수정됨: 시작 시간과 종료 시간을 함수로 넘겨줍니다.
                    startTimer(startTime, endTimeStr);
                } else {
                    alert("선택하신 시간대에 이미 선점된 예약이 겹쳐있습니다. 새로고침 후 다시 시도해주세요.");
                    location.reload();
                }
            })
            .catch(error => console.error('Error:', error));
        });

        resDateInput.addEventListener('change', function() {
            const selectedDate = this.value;
            if (!selectedDate) return;

            const roomId = document.getElementById("roomId").value;

            fetch('checkReservedTime.do?roomId=' + roomId + '&resDate=' + selectedDate)
                .then(response => response.json())
                .then(reservedTimes => {
                    selectedTimes = []; 
                    
                    timeButtons.forEach(btn => {
                        btn.disabled = false;
                        btn.classList.remove('active');
                    });
                    
                    clearInterval(timerInterval);
                    document.getElementById("timerBox").style.display = "none";
                    document.getElementById("submitBtn").disabled = true;
                    document.getElementById("holdBtn").style.display = "block";
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
            alert("먼저 [시간 선점하기] 버튼을 눌러 예약을 선점해주세요.");
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
    
    <!-- ★ 수정됨: 타이머 영역에 선택한 시간 범위를 띄워줄 텍스트 영역(holdTimeText) 추가 -->
    <div class="timer-box" id="timerBox">
        <span id="holdTimeText" style="color: #d32f2f; font-size: 16px;"></span><br>
        남은 시간 내에 예약을 확정해주세요 <span class="timer-text" id="timeDisplay">05:00</span>
    </div>

    <form action="reserveProcess.do" method="post" onsubmit="return validateForm();">
        <input type="hidden" id="resNo" name="resNo" value="">
        <input type="hidden" id="roomId" name="roomId" value="<%= roomInfo.getRoomId() %>">
        <input type="hidden" id="empNo" name="empNo" value="<%= loginEmp.getEmpNo() %>">
        
        <div class="form-group">
            <label for="resDate">예약 날짜</label>
            <input type="date" id="resDate" name="resDate" required>
        </div>
        
        <div class="form-group">
            <label>시간 선택 (연속 선택 가능)</label>
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
            <button type="button" class="btn-action btn-hold" id="holdBtn">선택한 시간 확정하기</button>
        </div>
        
        <div class="form-group">
            <label for="purpose">사용 목적</label>
            <input type="text" id="purpose" name="purpose" placeholder="예) 주간 회의, 클라이언트 미팅 등" required>
        </div>
        
        <div class="btn-group">
            <a href="main.jsp" class="btn-action btn-cancel">취소</a>
            <button type="submit" class="btn-action btn-submit" id="submitBtn" disabled>예약 확정하기</button>
        </div>
    </form>
</div>

</body>
</html>