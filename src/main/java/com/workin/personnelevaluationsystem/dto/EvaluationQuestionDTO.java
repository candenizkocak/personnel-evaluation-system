package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero; // For order index

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationQuestionDTO {

    private Integer questionID; // Null for new questions within a form

    // formID is needed if managing questions independently, but typically passed implicitly when nested in a form.
    // private Integer formID; // Not needed if always nested under EvaluationFormDTO for CRUD

    @NotBlank(message = "Question text cannot be blank")
    @Size(max = 500, message = "Question text cannot exceed 500 characters")
    private String questionText;

    @NotNull(message = "Question Type ID cannot be null")
    @Min(value = 1, message = "Question Type ID must be a positive integer")
    private Integer questionTypeID; // Foreign key to QuestionType

    @NotNull(message = "Weight cannot be null")
    @Min(value = 0, message = "Weight cannot be negative") // Assuming weight can be 0 or positive
    private Integer weight;

    @NotNull(message = "IsRequired status cannot be null")
    private Boolean isRequired;

    @NotNull(message = "Order index cannot be null")
    @PositiveOrZero(message = "Order index must be a non-negative integer")
    private Integer orderIndex;
}