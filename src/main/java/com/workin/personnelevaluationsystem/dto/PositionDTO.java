package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min; // For numeric values like IDs

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PositionDTO {

    private Integer positionID;

    @NotBlank(message = "Position title cannot be blank")
    @Size(max = 100, message = "Position title cannot exceed 100 characters")
    private String title;

    @Size(max = 500, message = "Position description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Department ID cannot be null")
    @Min(value = 1, message = "Department ID must be a positive integer")
    private Integer departmentID;

    private String departmentName; // ADD THIS FIELD for display purposes

    @NotNull(message = "IsManagement status cannot be null")
    private Boolean isManagement;
}