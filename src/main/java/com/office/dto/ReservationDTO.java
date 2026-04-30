package com.office.dto;

import java.sql.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private int resNo;           // 예약 번호 (PK)
    private int empNo;           // 예약자 사원번호 (FK)
    private String roomCode;     // 예약 방 코드 (FK)
    private Date resDate;        // 예약 날짜
    private String startTime;    // 시작 시간
    private String endTime;      // 종료 시간
    private String status;       // 예약 상태 (예약완료, 취소됨)
}