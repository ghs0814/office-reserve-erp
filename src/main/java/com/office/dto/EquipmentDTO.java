package com.office.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDTO {
    private int equipNo;         // 비품 번호 (PK)
    private String equipName;    // 비품명
    private int totalQty;        // 총 수량
    private int remainQty;       // 남은 수량
}