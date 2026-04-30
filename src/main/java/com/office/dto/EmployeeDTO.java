package com.office.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data                // Getter, Setter, ToString, EqualsAndHashCode 등을 한 번에 생성
@NoArgsConstructor   // 파라미터 없는 기본 생성자
@AllArgsConstructor  // 모든 필드를 포함하는 생성자
public class EmployeeDTO {
    private int empNo;
    private String loginId;
    private String loginPw;
    private String empName;
}