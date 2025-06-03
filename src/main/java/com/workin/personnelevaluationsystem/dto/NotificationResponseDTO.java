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
public class NotificationResponseDTO {

    private Integer notificationID;
    private Integer employeeID;
    private String employeeFullName; // Enriched data
    private String message;
    private LocalDateTime created;
    private Boolean isRead;
    private String relatedEntityType;
    private Integer relatedEntityID;
}