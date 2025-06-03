package com.workin.personnelevaluationsystem.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    private Integer employeeID;

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format") // Ensures it's a valid email structure
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phone;

    @NotNull(message = "Hire date cannot be null")
    @PastOrPresent(message = "Hire date cannot be in the future") // Ensures hire date is not in the future
    private LocalDate hireDate;

    @NotNull(message = "Position ID cannot be null")
    @Min(value = 1, message = "Position ID must be a positive integer")
    private Integer positionID;

    // Manager ID can be null if the employee has no manager (e.g., CEO)
    @Min(value = 1, message = "Manager ID must be a positive integer")
    private Integer managerID;

    @NotNull(message = "IsActive status cannot be null")
    private Boolean isActive;
}