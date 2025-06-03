package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.Valid; // For validating nested DTOs
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
    private Integer typeID; // Foreign key to EvaluationType

    @NotNull(message = "IsActive status cannot be null")
    private Boolean isActive;

    // Nested list of questions for creation/update
    // @Valid will ensure that each nested EvaluationQuestionDTO is also validated
    // @NotEmpty(message = "An evaluation form must have at least one question") // Uncomment if forms must have questions
    @Valid
    private List<EvaluationQuestionDTO> questions; // For creating/updating questions within the form
}