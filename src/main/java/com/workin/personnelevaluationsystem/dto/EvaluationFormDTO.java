package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationFormDTO {

    private Integer formID;

    @NotBlank(message = "Form title cannot be blank")
    @Size(max = 100, message = "Form title cannot exceed 100 characters")
    private String title;

    @Size(max = 500, message = "Form description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Evaluation Type ID cannot be null")
    @Min(value = 1, message = "Evaluation Type ID must be a positive integer")
    private Integer typeID;

    private String typeName; // Added for displaying the type name

    @NotNull(message = "IsActive status cannot be null")
    private Boolean isActive;

    // This list is now ONLY for DISPLAY purposes, not for binding on form submission.
    @Valid
    private List<EvaluationQuestionDTO> questions;
}