package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalStatusDTO {

    private Integer statusID;

    @NotBlank(message = "Goal status name cannot be blank")
    @Size(max = 50, message = "Goal status name cannot exceed 50 characters")
    private String name;

    @Size(max = 200, message = "Goal status description cannot exceed 200 characters")
    private String description;
}