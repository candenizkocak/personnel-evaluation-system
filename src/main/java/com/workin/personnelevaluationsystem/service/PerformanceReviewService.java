package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.EmployeeAverageScoreDTO; // Import new DTO
import com.workin.personnelevaluationsystem.dto.PerformanceReviewCreateDTO;
import com.workin.personnelevaluationsystem.dto.PerformanceReviewResponseDTO;
import com.workin.personnelevaluationsystem.dto.ReviewSubmissionDTO;

import java.util.List;
import java.util.Optional;

public interface PerformanceReviewService {
    PerformanceReviewResponseDTO createPerformanceReview(PerformanceReviewCreateDTO reviewDTO);
    Optional<PerformanceReviewResponseDTO> getPerformanceReviewById(Integer id);
    List<PerformanceReviewResponseDTO> getAllPerformanceReviews();

    void saveReviewDraft(Integer reviewId, ReviewSubmissionDTO submissionDTO);
    PerformanceReviewResponseDTO submitFinalReview(Integer reviewId, ReviewSubmissionDTO submissionDTO);

    List<PerformanceReviewResponseDTO> getReviewsByEmployee(Integer employeeId);
    List<PerformanceReviewResponseDTO> getReviewsByEvaluator(Integer evaluatorId);

    List<EmployeeAverageScoreDTO> getEmployeeAverageScores();

    List<EmployeeAverageScoreDTO> getEmployeeAverageScoresForManager(Integer managerId);

    List<PerformanceReviewResponseDTO> getReviewsForSubordinates(Integer managerId);

}