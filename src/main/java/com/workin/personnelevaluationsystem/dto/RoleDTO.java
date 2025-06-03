package com.workin.personnelevaluationsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set; // For permissions

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO {

    private Integer roleID;

    @NotBlank(message = "Role name cannot be blank")
    @Size(max = 50, message = "Role name cannot exceed 50 characters")
    private String name;

    @Size(max = 200, message = "Role description cannot exceed 200 characters")
    private String description;

    // Optionally, include a list of permission IDs or PermissionDTOs if needed for CRUD operations on roles
    // For simplicity for now, we'll just handle the basic role attributes.
    // If roles can be created/updated with associated permissions, this would be a Set<Integer> of permission IDs.
    private Set<Integer> permissionIDs; // For associating permissions during role creation/update
}