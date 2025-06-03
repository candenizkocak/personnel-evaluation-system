package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetencyLevelDTO {

    private Integer levelID;

    // competencyID is often implied by parent, but can be explicit for standalone.
    // private Integer competencyID;

    @NotNull(message = "Level cannot be null")
    @Min(value = 1, message = "Level must be a positive integer (e.g., 1, 2, 3)")
    private Integer level;

    @NotBlank(message = "Level description cannot be blank")
    @Size(max = 500, message = "Level description cannot exceed 500 characters")
    private String description;
}