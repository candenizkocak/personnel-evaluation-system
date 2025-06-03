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
public class NotificationCreateDTO {

    private Integer notificationID; // Null for creation

    @NotNull(message = "Employee ID cannot be null")
    @Min(value = 1, message = "Employee ID must be a positive integer")
    private Integer employeeID; // The recipient employee

    @NotBlank(message = "Message content cannot be blank")
    @Size(max = 500, message = "Message content cannot exceed 500 characters")
    private String message;

    // Created date is often set by the system, but allowed here for specific cases like importing old data
    private LocalDateTime created;

    @NotNull(message = "IsRead status cannot be null")
    private Boolean isRead;

    @Size(max = 50, message = "Related entity type cannot exceed 50 characters")
    private String relatedEntityType; // Optional: To link to a specific entity type

    private Integer relatedEntityID; // Optional: To link to a specific entity instance
}