package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackCreateDTO {

    private Integer feedbackID; // Null for creation

    @NotNull(message = "Sender ID cannot be null")
    @Min(value = 1, message = "Sender ID must be a positive integer")
    private Integer senderID;

    @NotNull(message = "Receiver ID cannot be null")
    @Min(value = 1, message = "Receiver ID must be a positive integer")
    private Integer receiverID;

    @NotNull(message = "Feedback Type ID cannot be null")
    @Min(value = 1, message = "Feedback Type ID must be a positive integer")
    private Integer feedbackTypeID;

    @NotBlank(message = "Feedback content cannot be blank")
    @Size(max = 4000, message = "Feedback content cannot exceed 4000 characters") // Max length for NVARCHAR(MAX)
    private String content;

    // Submission date is often set by the system, but allowed here if needed for specific cases
    private LocalDateTime submissionDate;

    @NotNull(message = "IsAnonymous status cannot be null")
    private Boolean isAnonymous;
}