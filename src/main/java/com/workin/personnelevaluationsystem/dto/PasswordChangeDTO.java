// src/main/java/com/workin/personnelevaluationsystem/dto/PasswordChangeDTO.java
package com.workin.personnelevaluationsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordChangeDTO {
    @NotBlank(message = "Old password cannot be blank")
    private String oldPassword;

    @NotBlank(message = "New password cannot be blank")
    @Size(min = 8, message = "New password must be at least 8 characters long")
    private String newPassword;

    @NotBlank(message = "Confirmation password cannot be blank")
    private String confirmPassword;
}