package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Integer userID;
    private String username;
    private Integer employeeID; // Just the ID, not the full EmployeeDTO
    private String employeeName; // For convenience, e.g., "John Doe"
    private LocalDateTime lastLogin;
    private Boolean isLocked;
    private Set<String> roles; // List of role names for display
}