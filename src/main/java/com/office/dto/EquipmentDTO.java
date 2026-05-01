package com.office.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 비품 마스터 정보 및 재고 데이터를 관리하는 DTO입니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDTO {
    private int eqNo;         // 비품 고유 번호
    private String eqName;     // 비품 명칭
    private int totalCount;    // 전체 보유 수량
    private int remainCount;   // 현재 대여 가능한 수량
}