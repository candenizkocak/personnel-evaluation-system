package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Roles") // Maps to the Roles table in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleID")
    private Integer roleID;

    @Column(name = "Name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "Description", length = 200)
    private String description;

    // Many-to-Many relationship with Users
    // The 'mappedBy' attribute indicates that the 'roles' field in the User entity
    // is the owner of the relationship, meaning it's responsible for managing the join table.
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>(); // Initialize to prevent NullPointerExceptions

    // Many-to-Many relationship with Permissions
    // The 'joinColumns' is for the current (Role) entity's foreign key in the join table
    // The 'inverseJoinColumns' is for the other (Permission) entity's foreign key in the join table
    @ManyToMany
    @JoinTable(
            name = "RolePermissions", // Name of the join table
            joinColumns = @JoinColumn(name = "RoleID"), // Column in RolePermissions table for Role
            inverseJoinColumns = @JoinColumn(name = "PermissionID") // Column in RolePermissions table for Permission
    )
    private Set<Permission> permissions = new HashSet<>();
}