package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDTO {

    // User ID is generally not sent for creation, but can be for updates
    private Integer userID;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    // Password is required for creation, optional for update (if not provided, keep current)
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotNull(message = "Employee ID cannot be null")
    @Min(value = 1, message = "Employee ID must be a positive integer")
    private Integer employeeID;

    // Roles IDs associated with the user
    // @NotEmpty(message = "User must have at least one role") // Uncomment if a user must have at least one role
    private Set<Integer> roleIDs;

    @NotNull(message = "IsLocked status cannot be null")
    private Boolean isLocked;
}