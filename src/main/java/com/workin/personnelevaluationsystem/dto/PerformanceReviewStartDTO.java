package com.workin.personnelevaluationsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PerformanceReviewStartDTO {

    @NotNull(message = "You must select an employee to review.")
    @Min(value = 1, message = "Invalid Employee ID.")
    private Integer employeeID;

    @NotNull(message = "You must select an evaluator.")
    @Min(value = 1, message = "Invalid Evaluator ID.")
    private Integer evaluatorID; // Typically the logged-in user

    @NotNull(message = "You must select an evaluation period.")
    @Min(value = 1, message = "Invalid Period ID.")
    private Integer periodID;

    @NotNull(message = "You must select an evaluation form.")
    @Min(value = 1, message = "Invalid Form ID.")
    private Integer formID;
}