package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CompetencyCategories") // Maps to the CompetencyCategories table in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetencyCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryID")
    private Integer categoryID;

    @Column(name = "Name", nullable = false, length = 100)
    private String name;

    @Column(name = "Description", length = 500)
    private String description;

    // One-to-Many relationship with Competencies
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Competency> competencies = new ArrayList<>();

    public void addCompetency(Competency competency) {
        if (competencies == null) {
            competencies = new ArrayList<>();
        }
        competencies.add(competency);
        competency.setCategory(this);
    }

    public void removeCompetency(Competency competency) {
        if (competencies != null) {
            competencies.remove(competency);
            competency.setCategory(null);
        }
    }
}