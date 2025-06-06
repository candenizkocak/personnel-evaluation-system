package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.PerformanceReviewCreateDTO;
import com.workin.personnelevaluationsystem.dto.PerformanceReviewResponseDTO;
import com.workin.personnelevaluationsystem.dto.ReviewSubmissionDTO;

import java.util.List;
import java.util.Optional;

public interface PerformanceReviewService {
    PerformanceReviewResponseDTO createPerformanceReview(PerformanceReviewCreateDTO reviewDTO);
    Optional<PerformanceReviewResponseDTO> getPerformanceReviewById(Integer id);
    List<PerformanceReviewResponseDTO> getAllPerformanceReviews();

    // Replaces the generic update with specific actions
    void saveReviewDraft(Integer reviewId, ReviewSubmissionDTO submissionDTO);
    PerformanceReviewResponseDTO submitFinalReview(Integer reviewId, ReviewSubmissionDTO submissionDTO);

    List<PerformanceReviewResponseDTO> getReviewsByEmployee(Integer employeeId);
    List<PerformanceReviewResponseDTO> getReviewsByEvaluator(Integer evaluatorId);
}