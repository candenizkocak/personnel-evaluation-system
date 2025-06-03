package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

@Entity
@Table(name = "ReviewResponses") // Maps to the ReviewResponses table in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ResponseID")
    private Integer responseID;

    // Many-to-One relationship with PerformanceReview
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReviewID") // Foreign key column in ReviewResponses table
    private PerformanceReview performanceReview;

    // Many-to-One relationship with EvaluationQuestion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QuestionID") // Foreign key column in ReviewResponses table
    private EvaluationQuestion evaluationQuestion;

    @Column(name = "ResponseText", columnDefinition = "NVARCHAR(MAX)") // NVARCHAR(MAX) in SQL
    private String responseText;

    @Column(name = "NumericResponse", precision = 5, scale = 2) // DECIMAL(5,2) in SQL
    private BigDecimal numericResponse;
}