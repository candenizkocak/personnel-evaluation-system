package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Goals") // Maps to the Goals table in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GoalID")
    private Integer goalID;

    // Many-to-One relationship with Employee
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeID") // Foreign key column in Goals table
    private Employee employee;

    @Column(name = "Title", nullable = false, length = 100)
    private String title;

    @Column(name = "Description", length = 500)
    private String description;

    // Many-to-One relationship with GoalType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GoalTypeID") // Foreign key column in Goals table
    private GoalType goalType;

    // Many-to-One relationship with GoalStatus
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StatusID") // Foreign key column in Goals table
    private GoalStatus goalStatus;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "TargetDate")
    private LocalDate targetDate;

    @Column(name = "CompletionDate")
    private LocalDate completionDate;

    @Column(name = "Progress", precision = 5, scale = 2) // DECIMAL(5,2) in SQL
    private BigDecimal progress;
}