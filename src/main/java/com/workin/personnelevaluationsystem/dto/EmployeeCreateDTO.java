package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat; // Import this

import java.time.LocalDate;
import java.util.Set; // Assuming this is still here for user roles, not strictly for employee

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeCreateDTO {

    private Integer employeeID;

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phone;

    @NotNull(message = "Hire date cannot be null")
    @PastOrPresent(message = "Hire date cannot be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // <--- ADD THIS LINE
    private LocalDate hireDate;

    @NotNull(message = "Position ID cannot be null")
    @Min(value = 1, message = "Position ID must be a positive integer")
    private Integer positionID;

    @Min(value = 1, message = "Manager ID must be a positive integer")
    private Integer managerID;

    @NotNull(message = "IsActive status cannot be null")
    private Boolean isActive;
}