package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList; // Use ArrayList for indexed lists
import java.util.List;

@Entity
@Table(name = "EvaluationForms") // Maps to the EvaluationForms table in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FormID")
    private Integer formID;

    @Column(name = "Title", nullable = false, length = 100)
    private String title;

    @Column(name = "Description", length = 500)
    private String description;

    // Many-to-One relationship with EvaluationType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TypeID") // Foreign key column in EvaluationForms table
    private EvaluationType evaluationType;

    @Column(name = "IsActive")
    private Boolean isActive;

    // One-to-Many relationship with EvaluationQuestions
    // Questions are ordered, so we might need a specific type or sort order.
    // For now, cascade and orphanRemoval are set to manage questions directly from the form.
    @OneToMany(mappedBy = "evaluationForm", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("orderIndex ASC") // Order questions by their orderIndex
    private List<EvaluationQuestion> questions = new ArrayList<>();

    // Helper method to add a question and set its form reference
    public void addQuestion(EvaluationQuestion question) {
        if (questions == null) {
            questions = new ArrayList<>();
        }
        questions.add(question);
        question.setEvaluationForm(this);
    }

    // Helper method to remove a question
    public void removeQuestion(EvaluationQuestion question) {
        if (questions != null) {
            questions.remove(question);
            question.setEvaluationForm(null);
        }
    }
}