package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Entity
@Table(name = "EmployeeCompetencies") // Maps to the EmployeeCompetencies table in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(EmployeeCompetencyId.class) // Specifies the composite primary key class
public class EmployeeCompetency {

    @Id // Part of the composite key
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeID")
    private Employee employee;

    @Id // Part of the composite key
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompetencyID")
    private Competency competency;

    @Id // Part of the composite key
    @Column(name = "AssessmentDate")
    private LocalDate assessmentDate; // DATE in SQL maps to LocalDate in Java

    // The remaining fields are non-key attributes
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LevelID") // Foreign key to CompetencyLevels table
    private CompetencyLevel level; // Represents the assessed level of the competency

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AssessedBy") // Foreign key to Employees table
    private Employee assessedBy; // The employee who performed the assessment
}