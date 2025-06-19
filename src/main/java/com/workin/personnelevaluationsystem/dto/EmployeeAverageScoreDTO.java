package com.workin.personnelevaluationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeAverageScoreDTO {
    private Integer employeeId;
    private String employeeFullName;
    private BigDecimal averageScore;
    private int reviewCount; // Number of reviews contributing to the average
}