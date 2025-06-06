package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponseDTO {

    private Integer employeeID;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate hireDate;
    private Boolean isActive;

    // Enriched fields for display purposes
    private Integer positionID;
    private String positionTitle; // Title of the associated position
    private Integer managerID;
    private String managerFullName; // Full name of the manager
}