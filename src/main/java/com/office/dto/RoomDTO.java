package com.office.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private String roomCode;     // 방 코드 (PK)
    private String roomName;     // 방 이름
    private int capacity;        // 수용 인원
    private String hasEquipment; // 기자재 유무 (Y/N)
    private String description;  // 방 상세 설명
}