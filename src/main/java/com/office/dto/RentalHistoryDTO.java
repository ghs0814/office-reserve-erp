package com.office.dto;

import java.sql.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 비품 대여 신청 내역 및 5단계 결재 진행 상태를 관리하는 DTO입니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalHistoryDTO {
    private int rentalNo;       // 대여 기록 고유 번호 (PK)
    private int empNo;          // 대여를 신청한 사원 번호 (FK)
    private int eqNo;           // 대여 대상 비품 번호 (FK)
    private String eqName;      // [조인용] 화면에 표시할 비품 이름
    private Date rentalDate;    // 대여 시작일
    private Date returnDate;    // 반납 예정일
    private String status;      // 결재/대여 상태 (승인대기, 대여중, 반려, 반납완료 등)
    private int approvalStep;   // 현재 진행 중인 결재 단계 (1 ~ 5단계)
    
    // 다단계 결재 시스템의 각 단계별 관리자 서명 기록
    private String sign1;       // 1단계 서명 (보통 기안자 본인)
    private String sign2;       // 2단계 중간 결재자 서명
    private String sign3;       // 3단계 중간 결재자 서명
    private String sign4;       // 4단계 중간 결재자 서명
    private String sign5;       // 5단계 최종 승인자 서명
}