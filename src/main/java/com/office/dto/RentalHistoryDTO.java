package com.office.dto;

import java.sql.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalHistoryDTO {
    private int rentalNo;       
    private int empNo;          
    private int eqNo;           
    private String eqName;      
    private Date rentalDate;    
    private Date returnDate;    
    private String status;      
    private int approvalStep;   
    
    private String sign1;       
    private String sign2;       
    private String sign3;       
    private String sign4;       
    private String sign5;       
    
    private String title;            
    private java.sql.Date sign1Date; 
    private java.sql.Date sign2Date; 
    private java.sql.Date sign3Date; 
    private java.sql.Date sign4Date; 
    private java.sql.Date sign5Date; 

    private String empName;        
    private int empLevel;          
    private int totalCount;        // 비품 총 수량 (EquipmentDTO와 완전 일치)
    private int remainCount;       // 비품 잔여 수량 (EquipmentDTO와 완전 일치)
}