package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Employees") // Maps to the Employees table in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmployeeID") // Maps to EmployeeID column
    private Integer employeeID;

    @Column(name = "FirstName", nullable = false, length = 50) // Maps to FirstName column
    private String firstName;

    @Column(name = "LastName", nullable = false, length = 50) // Maps to LastName column
    private String lastName;

    @Column(name = "Email", nullable = false, unique = true, length = 100) // Maps to Email, unique constraint
    private String email;

    @Column(name = "Phone", length = 20) // Maps to Phone column
    private String phone;

    @Column(name = "HireDate", nullable = false) // Maps to HireDate column
    private LocalDate hireDate; // DATE in SQL maps to LocalDate in Java

    // Many-to-One relationship with Position (many Employees can hold one Position)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PositionID") // Foreign key column in Employees table
    private Position position;

    // Self-referencing Many-to-One relationship for Manager
    // An Employee can have one Manager (who is also an Employee)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ManagerID") // Foreign key column in Employees table
    private Employee manager;

    // Self-referencing One-to-Many relationship for Subordinates
    // A Manager (Employee) can have many Subordinates (Employees)
    // mappedBy indicates the field in the *subordinate* Employee entity that owns this relationship (the 'manager' field)
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> subordinates; // List of employees reporting to this employee

    @Column(name = "IsActive") // Maps to IsActive column
    private Boolean isActive; // BIT in SQL maps to Boolean in Java
}