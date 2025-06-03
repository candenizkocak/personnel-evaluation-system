package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDTO {

    private Integer responseID; // Will be null for new responses

    @NotNull(message = "Question ID cannot be null")
    @Min(value = 1, message = "Question ID must be a positive integer")
    private Integer questionID; // Foreign key to EvaluationQuestion

    @Size(max = 4000, message = "Response text cannot exceed 4000 characters") // Max length for NVARCHAR(MAX) if it was restricted, though MAX is very large.
    private String responseText;

    @DecimalMin(value = "0.00", message = "Numeric response cannot be negative") // Min value for score
    private BigDecimal numericResponse; // Can be null if question is text-based
}