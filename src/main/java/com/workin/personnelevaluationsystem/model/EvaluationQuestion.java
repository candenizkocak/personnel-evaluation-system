package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "EvaluationQuestions") // Maps to the EvaluationQuestions table in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QuestionID")
    private Integer questionID;

    // Many-to-One relationship with EvaluationForm
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FormID") // Foreign key column in EvaluationQuestions table
    private EvaluationForm evaluationForm;

    @Column(name = "QuestionText", nullable = false, length = 500)
    private String questionText;

    // Many-to-One relationship with QuestionType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QuestionTypeID") // Foreign key column in EvaluationQuestions table
    private QuestionType questionType;

    @Column(name = "Weight")
    private Integer weight;

    @Column(name = "IsRequired")
    private Boolean isRequired;

    @Column(name = "OrderIndex", nullable = false)
    private Integer orderIndex;
}