package com.example.myapplication.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "esbi_data")
@Data
public class EsbiData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    
    private BigDecimal employeeIncome;
    private BigDecimal selfEmployedIncome;
    private BigDecimal businessOwnerIncome;
    private BigDecimal investorIncome;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
