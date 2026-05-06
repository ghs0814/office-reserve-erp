package com.office.dto;

import java.sql.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveHistoryDTO {
	private int leaveNo; // 휴가 번호 (PK)
	private int empNo; // 사번
	private Date startDate; // 시작일
	private Date endDate; // 종료일
	private int useDays; // 사용 일수 (주말 제외)
	private String reason; // 사유 (기본값: 개인 용무)
	private String status; // 상태 (승인대기, 반려됨, 승인완료)
	private int approvalStep; // 현재 결재 단계 (1~5)

	// 결재자 서명 및 날짜
	private String sign1;
	private Date sign1Date;
	private String sign2;
	private Date sign2Date;
	private String sign3;
	private Date sign3Date;
	private String sign4;
	private Date sign4Date;
	private String sign5;
	private Date sign5Date;

	// 조인용 필드
	private String empName;
	private String dept;
	// 기존 필드들 아래에 추가
	private int empLevel; // 기안자의 직급 (열람 권한 체크용)
}