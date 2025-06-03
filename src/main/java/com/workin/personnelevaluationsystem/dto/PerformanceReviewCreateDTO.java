package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotEmpty; // For list of responses

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceReviewCreateDTO {

    private Integer reviewID; // Will be null for new reviews

    @NotNull(message = "Employee ID cannot be null")
    @Min(value = 1, message = "Employee ID must be a positive integer")
    private Integer employeeID; // The employee being reviewed

    @NotNull(message = "Evaluator ID cannot be null")
    @Min(value = 1, message = "Evaluator ID must be a positive integer")
    private Integer evaluatorID; // The employee performing the review

    @NotNull(message = "Period ID cannot be null")
    @Min(value = 1, message = "Period ID must be a positive integer")
    private Integer periodID; // The evaluation period

    @NotNull(message = "Form ID cannot be null")
    @Min(value = 1, message = "Form ID must be a positive integer")
    private Integer formID; // The evaluation form used

    // SubmissionDate can be set by the system, but might be provided for specific cases
    private LocalDateTime submissionDate;

    @NotBlank(message = "Status cannot be blank")
    @Size(max = 20, message = "Status cannot exceed 20 characters")
    // Consider an enum or a predefined list for status in a real app
    private String status; // e.g., 'Draft', 'Submitted'

    @DecimalMin(value = "0.00", message = "Final score cannot be negative")
    @DecimalMax(value = "100.00", message = "Final score cannot exceed 100.00")
    private BigDecimal finalScore; // Can be null if review is a draft

    @Valid // Ensures nested ReviewResponseDTOs are also validated
    @NotEmpty(message = "A performance review must have at least one response")
    private List<ReviewResponseDTO> reviewResponses; // List of responses for the review
}