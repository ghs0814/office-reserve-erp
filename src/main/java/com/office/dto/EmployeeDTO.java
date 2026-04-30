package com.office.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private int empNo;
    private String loginId;
    private String loginPw;
    private String empName;
    private int empLevel; // 5단계 결재 시스템용 권한 레벨 (일반 0, 최고관리자 5)
}