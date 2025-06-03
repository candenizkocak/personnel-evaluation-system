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
public class QuestionTypeDTO {

    private Integer questionTypeID;

    @NotBlank(message = "Question type name cannot be blank")
    @Size(max = 50, message = "Question type name cannot exceed 50 characters")
    private String name;

    @Size(max = 200, message = "Question type description cannot exceed 200 characters")
    private String description;
}