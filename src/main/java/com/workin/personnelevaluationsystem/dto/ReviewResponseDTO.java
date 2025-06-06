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

    private Integer responseID;

    @NotNull(message = "Question ID cannot be null")
    @Min(value = 1, message = "Question ID must be a positive integer")
    private Integer questionID;

    // Add this field to hold the question text for display on the form
    private String questionText;

    @Size(max = 4000, message = "Response text cannot exceed 4000 characters")
    private String responseText;

    @DecimalMin(value = "0.00", message = "Numeric response cannot be negative")
    private BigDecimal numericResponse;
}