package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalDTO {

    private Integer goalID;

    @NotNull(message = "Employee ID cannot be null")
    @Min(value = 1, message = "Employee ID must be a positive integer")
    private Integer employeeID; // Foreign key to Employee

    @NotBlank(message = "Goal title cannot be blank")
    @Size(max = 100, message = "Goal title cannot exceed 100 characters")
    private String title;

    @Size(max = 500, message = "Goal description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Goal Type ID cannot be null")
    @Min(value = 1, message = "Goal Type ID must be a positive integer")
    private Integer goalTypeID; // Foreign key to GoalType

    @NotNull(message = "Goal Status ID cannot be null")
    @Min(value = 1, message = "Goal Status ID must be a positive integer")
    private Integer statusID; // Foreign key to GoalStatus

    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDate startDate;

    // @FutureOrPresent(message = "Target date cannot be in the past") // Optional, depends on business logic
    private LocalDate targetDate;

    @PastOrPresent(message = "Completion date cannot be in the future")
    private LocalDate completionDate;

    @DecimalMin(value = "0.00", message = "Progress cannot be less than 0")
    @DecimalMax(value = "100.00", message = "Progress cannot exceed 100")
    private BigDecimal progress; // Default 0.00 in DB
}