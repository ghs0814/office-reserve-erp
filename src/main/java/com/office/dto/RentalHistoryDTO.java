package com.office.dto;

import java.sql.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalHistoryDTO {
    private int rentalNo;       // 대여 번호
    private int empNo;          // 사원 번호
    private int eqNo;           // 비품 번호
    private Date rentalDate;    // 대여일
    private Date returnDate;    // 반납일
    private String status;      // 결재/대여 상태
    private int approvalStep;   // 결재 단계
}