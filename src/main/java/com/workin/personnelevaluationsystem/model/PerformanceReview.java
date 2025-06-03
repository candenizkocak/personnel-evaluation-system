package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PerformanceReviews") // Maps to the PerformanceReviews table in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReviewID")
    private Integer reviewID;

    // Many-to-One relationship with Employee (employee being reviewed)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeID") // Foreign key column in PerformanceReviews table
    private Employee employee;

    // Many-to-One relationship with Employee (evaluator)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EvaluatorID") // Foreign key column in PerformanceReviews table
    private Employee evaluator;

    // Many-to-One relationship with EvaluationPeriod
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PeriodID") // Foreign key column in PerformanceReviews table
    private EvaluationPeriod evaluationPeriod;

    // Many-to-One relationship with EvaluationForm
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FormID") // Foreign key column in PerformanceReviews table
    private EvaluationForm evaluationForm;

    @Column(name = "SubmissionDate")
    private LocalDateTime submissionDate; // DATETIME in SQL maps to LocalDateTime in Java

    @Column(name = "Status", length = 20)
    private String status; // e.g., 'Draft', 'Submitted', 'Approved', 'Rejected'

    @Column(name = "FinalScore", precision = 5, scale = 2) // DECIMAL(5,2) in SQL
    private BigDecimal finalScore;

    // One-to-Many relationship with ReviewResponses
    // CascadeType.ALL for managing responses through the review
    // orphanRemoval = true: If a response is removed from the review.getResponses() list, it's deleted from DB
    @OneToMany(mappedBy = "performanceReview", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReviewResponse> reviewResponses = new ArrayList<>();

    // Helper methods to manage bi-directional relationship with ReviewResponse
    public void addReviewResponse(ReviewResponse response) {
        if (reviewResponses == null) {
            reviewResponses = new ArrayList<>();
        }
        reviewResponses.add(response);
        response.setPerformanceReview(this);
    }

    public void removeReviewResponse(ReviewResponse response) {
        if (reviewResponses != null) {
            reviewResponses.remove(response);
            response.setPerformanceReview(null);
        }
    }
}