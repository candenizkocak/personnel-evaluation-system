package com.workin.personnelevaluationsystem.repository;

import com.workin.personnelevaluationsystem.model.ReviewResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewResponseRepository extends JpaRepository<ReviewResponse, Integer> {
    List<ReviewResponse> findByPerformanceReview_ReviewID(Integer reviewId);
    List<ReviewResponse> findByEvaluationQuestion_QuestionID(Integer questionId);
    // You might need a more specific query for a response to a specific question within a specific review
    Optional<ReviewResponse> findByPerformanceReview_ReviewIDAndEvaluationQuestion_QuestionID(Integer reviewId, Integer questionId);
}