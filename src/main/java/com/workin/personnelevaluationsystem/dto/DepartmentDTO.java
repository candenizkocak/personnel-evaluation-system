package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.NotBlank; // Import from jakarta.validation
import jakarta.validation.constraints.Size;   // Import from jakarta.validation
import jakarta.validation.constraints.NotNull; // Import from jakarta.validation

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDTO {

    private Integer departmentID; // ID is usually not validated on input for creation/update

    @NotBlank(message = "Department name cannot be blank") // Name must not be null and must contain at least one non-whitespace character
    @Size(max = 100, message = "Department name cannot exceed 100 characters") // Max length constraint
    private String name;

    @Size(max = 500, message = "Department description cannot exceed 500 characters") // Max length constraint
    private String description;

    @NotNull(message = "IsActive status cannot be null") // Boolean must be true or false, not missing
    private Boolean isActive;
}