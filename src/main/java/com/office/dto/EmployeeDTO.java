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
	private int empNo;      // 사번
    private String empPw;      // 비밀번호
    private String empName;    // 이름
    private int empLevel;      // 직급
    private String manager;    // 관리자 여부 (Y/N)
}