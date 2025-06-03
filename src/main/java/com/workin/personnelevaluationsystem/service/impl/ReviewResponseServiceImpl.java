package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.ReviewResponseDTO;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.ReviewResponse;
import com.workin.personnelevaluationsystem.repository.ReviewResponseRepository;
import com.workin.personnelevaluationsystem.service.ReviewResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewResponseServiceImpl implements ReviewResponseService {

    private final ReviewResponseRepository responseRepository;

    @Autowired
    public ReviewResponseServiceImpl(ReviewResponseRepository responseRepository) {
        this.responseRepository = responseRepository;
    }

    private ReviewResponseDTO convertToDto(ReviewResponse response) {
        if (response == null) return null;
        return ReviewResponseDTO.builder()
                .responseID(response.getResponseID())
                .questionID(response.getEvaluationQuestion() != null ? response.getEvaluationQuestion().getQuestionID() : null)
                .responseText(response.getResponseText())
                .numericResponse(response.getNumericResponse())
                .build();
    }

    // We don't need convertToEntity for direct creation/update here as they're primarily handled by PerformanceReviewService.
    // If standalone updates are needed, add logic similar to other services.

    @Override
    public Optional<ReviewResponseDTO> getReviewResponseById(Integer id) {
        return responseRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<ReviewResponseDTO> getResponsesByReviewId(Integer reviewId) {
        return responseRepository.findByPerformanceReview_ReviewID(reviewId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReviewResponse(Integer id) {
        if (!responseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Review response not found with ID: " + id);
        }
        responseRepository.deleteById(id);
    }
}