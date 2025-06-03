package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Entity
@Table(name = "Positions") // Maps to the Positions table in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PositionID") // Maps to PositionID column
    private Integer positionID;

    @Column(name = "Title", nullable = false, length = 100) // Maps to Title column
    private String title;

    @Column(name = "Description", length = 500) // Maps to Description column
    private String description;

    // Many-to-One relationship with Department (many Positions belong to one Department)
    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetching for performance (load Department only when accessed)
    @JoinColumn(name = "DepartmentID") // Specifies the foreign key column in the Positions table
    private Department department;

    @Column(name = "IsManagement") // Maps to IsManagement column
    private Boolean isManagement; // BIT in SQL maps to Boolean in Java

    // One-to-Many relationship with Employee (a Position can have many Employees)
    // mappedBy indicates the field in the Employee entity that owns the relationship
    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees;
}