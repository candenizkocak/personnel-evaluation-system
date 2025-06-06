package com.workin.personnelevaluationsystem.dto;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * A dedicated DTO for submitting the responses of a performance review form.
 */
@Data
@NoArgsConstructor
public class ReviewSubmissionDTO {

    @Valid // Ensures nested validation of each response
    private List<ReviewResponseDTO> reviewResponses;

}