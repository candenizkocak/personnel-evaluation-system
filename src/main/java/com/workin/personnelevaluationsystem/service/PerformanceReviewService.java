package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.PerformanceReviewCreateDTO;
import com.workin.personnelevaluationsystem.dto.PerformanceReviewResponseDTO;

import java.util.List;
import java.util.Optional;

public interface PerformanceReviewService {
    PerformanceReviewResponseDTO createPerformanceReview(PerformanceReviewCreateDTO reviewDTO);
    Optional<PerformanceReviewResponseDTO> getPerformanceReviewById(Integer id);
    List<PerformanceReviewResponseDTO> getAllPerformanceReviews();
    PerformanceReviewResponseDTO updatePerformanceReview(Integer id, PerformanceReviewCreateDTO reviewDetailsDTO);
    void deletePerformanceReview(Integer id);
    // Add more specific methods, e.g., for filtering reviews
    List<PerformanceReviewResponseDTO> getReviewsByEmployee(Integer employeeId);
    List<PerformanceReviewResponseDTO> getReviewsByEvaluator(Integer evaluatorId);
}