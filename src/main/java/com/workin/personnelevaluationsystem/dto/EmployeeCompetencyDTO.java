package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeCompetencyDTO {

    @NotNull(message = "Employee ID cannot be null")
    @Min(value = 1, message = "Employee ID must be a positive integer")
    private Integer employeeID;

    @NotNull(message = "Competency ID cannot be null")
    @Min(value = 1, message = "Competency ID must be a positive integer")
    private Integer competencyID;

    @NotNull(message = "Assessment date cannot be null")
    @PastOrPresent(message = "Assessment date cannot be in the future")
    private LocalDate assessmentDate;

    @NotNull(message = "Level ID cannot be null")
    @Min(value = 1, message = "Level ID must be a positive integer")
    private Integer levelID; // Foreign key to CompetencyLevels

    @NotNull(message = "Assessed By Employee ID cannot be null")
    @Min(value = 1, message = "Assessed By Employee ID must be a positive integer")
    private Integer assessedByID; // Foreign key to Employees (the assessor)

    // For response, might want to include descriptive names for convenience
    private String employeeName;
    private String competencyName;
    private String levelDescription;
    private String assessedByName;
}