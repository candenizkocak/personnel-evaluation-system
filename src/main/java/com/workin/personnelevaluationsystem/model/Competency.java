package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Competencies") // Maps to the Competencies table in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Competency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CompetencyID")
    private Integer competencyID;

    @Column(name = "Name", nullable = false, length = 100)
    private String name;

    @Column(name = "Description", length = 500)
    private String description;

    // Many-to-One relationship with CompetencyCategory
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryID") // Foreign key column in Competencies table
    private CompetencyCategory category;

    // One-to-Many relationship with CompetencyLevels
    @OneToMany(mappedBy = "competency", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("level ASC") // Order levels by their numeric level
    private List<CompetencyLevel> levels = new ArrayList<>();

    public void addLevel(CompetencyLevel level) {
        if (levels == null) {
            levels = new ArrayList<>();
        }
        levels.add(level);
        level.setCompetency(this);
    }

    public void removeLevel(CompetencyLevel level) {
        if (levels != null) {
            levels.remove(level);
            level.setCompetency(null);
        }
    }
}