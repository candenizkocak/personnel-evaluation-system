package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "CompetencyLevels") // Maps to the CompetencyLevels table in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetencyLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LevelID")
    private Integer levelID;

    // Many-to-One relationship with Competency
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompetencyID") // Foreign key column in CompetencyLevels table
    private Competency competency;

    @Column(name = "Level", nullable = false)
    private Integer level; // The numeric level, e.g., 1, 2, 3

    @Column(name = "Description", nullable = false, length = 500)
    private String description;
}