package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import jakarta.validation.constraints.FutureOrPresent; // For dates

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationPeriodDTO {

    private Integer periodID;

    @NotBlank(message = "Period name cannot be blank")
    @Size(max = 100, message = "Period name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    // Consider a custom validator for endDate after startDate or handle in service
    private LocalDate endDate;

    @NotNull(message = "IsActive status cannot be null")
    private Boolean isActive;
}