package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionDTO {

    private Integer permissionID;

    @NotBlank(message = "Permission name cannot be blank")
    @Size(max = 100, message = "Permission name cannot exceed 100 characters")
    private String name;

    @Size(max = 200, message = "Permission description cannot exceed 200 characters")
    private String description;
}