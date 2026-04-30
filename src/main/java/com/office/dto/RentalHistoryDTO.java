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
    private String status;      // 결재/대여 상태 (승인대기, 대여중, 반려 등)
    private int approvalStep;   // 현재 결재 단계 (1 ~ 5)
    
    // 5단계 승인 기록용 필드
    private String sign1;
    private String sign2;
    private String sign3;
    private String sign4;
    private String sign5;
}