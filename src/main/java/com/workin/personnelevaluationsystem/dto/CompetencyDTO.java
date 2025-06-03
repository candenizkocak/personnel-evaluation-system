package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetencyDTO {

    private Integer competencyID;

    @NotBlank(message = "Competency name cannot be blank")
    @Size(max = 100, message = "Competency name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Competency description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Category ID cannot be null")
    @Min(value = 1, message = "Category ID must be a positive integer")
    private Integer categoryID; // Foreign key to CompetencyCategory

    @Valid // For nested levels
    private List<CompetencyLevelDTO> levels; // Optional: for nested create/update
}
