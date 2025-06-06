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
@Table(name = "Permissions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(exclude = {"roles"}) // <--- EXCLUDE relationships
@ToString(exclude = {"roles"}) // <--- EXCLUDE relationships
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PermissionID")
    private Integer permissionID;

    @Column(name = "Name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "Description", length = 200)
    private String description;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();
}