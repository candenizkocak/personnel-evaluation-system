package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceReviewResponseDTO {

    private Integer reviewID;
    private Integer employeeID;
    private String employeeFullName; // e.g., "John Doe"
    private Integer evaluatorID;
    private String evaluatorFullName; // e.g., "Jane Smith"
    private Integer periodID;
    private String periodName; // e.g., "Annual 2023"
    private Integer formID;
    private String formTitle; // e.g., "Annual Performance Review Form"
    private LocalDateTime submissionDate;
    private String status;
    private BigDecimal finalScore;
    private List<ReviewResponseDTO> reviewResponses; // Nested responses
}