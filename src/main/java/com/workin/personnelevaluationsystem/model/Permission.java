package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Permissions") // Maps to the Permissions table in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PermissionID")
    private Integer permissionID;

    @Column(name = "Name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "Description", length = 200)
    private String description;

    // Many-to-Many relationship with Roles
    // mappedBy indicates the field in the Role entity that owns the relationship
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();
}