package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackResponseDTO {

    private Integer feedbackID;
    private Integer senderID;
    private String senderFullName; // Enriched data
    private Integer receiverID;
    private String receiverFullName; // Enriched data
    private Integer feedbackTypeID;
    private String feedbackTypeName; // Enriched data
    private String content;
    private LocalDateTime submissionDate;
    private Boolean isAnonymous;
}