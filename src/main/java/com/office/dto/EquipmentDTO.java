package com.office.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDTO {
    private int eqNo;
    private String eqName;
    private int totalCount;
    private int remainCount;
}