package com.office.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 사원 정보 및 시스템 권한 데이터를 관리하는 DTO입니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private int empNo;      // 사원 번호 (고유 식별자)
    private String loginId; // 로그인용 아이디
    private String loginPw; // 로그인용 비밀번호
    private String empName; // 사원 성명
    private int empLevel;   // 5단계 결재 시스템용 권한 레벨 (일반 0, 최고관리자 5)
}