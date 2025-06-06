package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "Users")
@Data // Lombok handles getters/setters
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"employee", "roles"}) // Crucial for Hibernate performance/avoiding loops
@ToString(exclude = {"passwordHash", "employee", "roles"}) // Exclude sensitive/recursive fields from toString
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Integer userID;

    @Column(name = "Username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "PasswordHash", nullable = false, length = 128) // Store hashed password
    private String passwordHash;

    // One-to-One relationship with Employee
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeID", unique = true) // EmployeeID column in Users table
    private Employee employee;

    @Column(name = "LastLogin")
    private LocalDateTime lastLogin;

    @Column(name = "IsLocked")
    private Boolean isLocked;

    // Many-to-Many relationship with Roles
    @ManyToMany(fetch = FetchType.EAGER) // Fetch roles eagerly, common for authentication
    @JoinTable(
            name = "UserRoles", // Name of the join table
            joinColumns = @JoinColumn(name = "UserID"), // Column in UserRoles table for User
            inverseJoinColumns = @JoinColumn(name = "RoleID") // Column in UserRoles table for Role
    )
    private Set<Role> roles = new HashSet<>();

    // --- UserDetails interface methods ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Map our custom Role objects to Spring Security's GrantedAuthority
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())) // Prefix with "ROLE_" for convention
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.passwordHash;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // For now, assume accounts don't expire
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isLocked != null ? !this.isLocked : true; // If isLocked is null, default to not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // For now, assume credentials don't expire
    }

    @Override
    public boolean isEnabled() {
        return true; // For now, assume all users are enabled
    }
}