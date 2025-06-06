package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode; // Import
import lombok.ToString; // Import

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Roles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(exclude = {"users", "permissions"}) // <--- EXCLUDE relationships
@ToString(exclude = {"users", "permissions"}) // <--- EXCLUDE relationships
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleID")
    private Integer roleID;

    @Column(name = "Name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "Description", length = 200)
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "RolePermissions",
            joinColumns = @JoinColumn(name = "RoleID"),
            inverseJoinColumns = @JoinColumn(name = "PermissionID")
    )
    private Set<Permission> permissions = new HashSet<>();
}