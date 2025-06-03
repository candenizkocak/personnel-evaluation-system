package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users") // Maps to the Users table in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Integer userID;

    @Column(name = "Username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "PasswordHash", nullable = false, length = 128) // Store hashed password
    private String passwordHash;

    // One-to-One relationship with Employee
    // UserID is the primary key and also the foreign key to Employee.
    // It's a best practice to make the Employee side the owning side if it's the primary entity.
    // However, based on the SQL `EmployeeID INT FOREIGN KEY REFERENCES Employees(EmployeeID)` in `Users` table,
    // it seems User table holds the FK. This means User is the owning side.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeID", unique = true) // EmployeeID column in Users table
    private Employee employee;

    @Column(name = "LastLogin")
    private LocalDateTime lastLogin;

    @Column(name = "IsLocked")
    private Boolean isLocked;

    // Many-to-Many relationship with Roles
    // The 'joinColumns' is for the current (User) entity's foreign key in the join table
    // The 'inverseJoinColumns' is for the other (Role) entity's foreign key in the join table
    @ManyToMany(fetch = FetchType.EAGER) // Fetch roles eagerly, common for authentication
    @JoinTable(
            name = "UserRoles", // Name of the join table
            joinColumns = @JoinColumn(name = "UserID"), // Column in UserRoles table for User
            inverseJoinColumns = @JoinColumn(name = "RoleID") // Column in UserRoles table for Role
    )
    private Set<Role> roles = new HashSet<>();
}