package com.office.dto;

import java.sql.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalHistoryDTO {
    private int historyNo;       // 이력 번호 (PK)
    private int empNo;           // 사원번호 (FK)
    private int equipNo;         // 비품 번호 (FK)
    private Date rentalDate;     // 대여 날짜
    private String status;       // 대여 상태 (대여중, 반납완료)
}