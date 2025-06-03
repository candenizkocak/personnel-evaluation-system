package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.ReviewResponseDTO;

import java.util.Optional;
import java.util.List;

public interface ReviewResponseService {
    // Create is often done via PerformanceReview. Update/Delete might be standalone.
    // ReviewResponseDTO createReviewResponse(Integer reviewId, ReviewResponseDTO responseDTO); // If allowing standalone creation
    Optional<ReviewResponseDTO> getReviewResponseById(Integer id);
    List<ReviewResponseDTO> getResponsesByReviewId(Integer reviewId);
    // ReviewResponseDTO updateReviewResponse(Integer id, ReviewResponseDTO responseDetailsDTO); // If allowing standalone update
    void deleteReviewResponse(Integer id);
}