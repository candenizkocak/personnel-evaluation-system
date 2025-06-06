package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.PerformanceReviewCreateDTO;
import com.workin.personnelevaluationsystem.dto.PerformanceReviewResponseDTO;
import com.workin.personnelevaluationsystem.dto.ReviewSubmissionDTO; // Import the new DTO
import com.workin.personnelevaluationsystem.service.PerformanceReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/performance-reviews")
public class PerformanceReviewController {

    private final PerformanceReviewService reviewService;

    @Autowired
    public PerformanceReviewController(PerformanceReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<PerformanceReviewResponseDTO> createPerformanceReview(@Valid @RequestBody PerformanceReviewCreateDTO reviewDTO) {
        PerformanceReviewResponseDTO createdReview = reviewService.createPerformanceReview(reviewDTO);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerformanceReviewResponseDTO> getPerformanceReviewById(@PathVariable Integer id) {
        return reviewService.getPerformanceReviewById(id)
                .map(reviewDTO -> new ResponseEntity<>(reviewDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<PerformanceReviewResponseDTO>> getAllPerformanceReviews() {
        List<PerformanceReviewResponseDTO> reviews = reviewService.getAllPerformanceReviews();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    /**
     * Replaces the generic PUT mapping with specific actions for an API.
     * This endpoint saves the progress of a review without finalizing it.
     */
    @PutMapping("/{id}/draft")
    public ResponseEntity<Void> saveReviewDraft(@PathVariable Integer id, @Valid @RequestBody ReviewSubmissionDTO submissionDTO) {
        reviewService.saveReviewDraft(id, submissionDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content is appropriate for a save action
    }

    /**
     * This endpoint finalizes and submits a review, calculating its score.
     */
    @PostMapping("/{id}/submit")
    public ResponseEntity<PerformanceReviewResponseDTO> submitFinalReview(@PathVariable Integer id, @Valid @RequestBody ReviewSubmissionDTO submissionDTO) {
        PerformanceReviewResponseDTO finalReview = reviewService.submitFinalReview(id, submissionDTO);
        return new ResponseEntity<>(finalReview, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformanceReview(@PathVariable Integer id) {
        // Note: The service layer currently doesn't have a delete method.
        // If you need API deletion, you must add `void deletePerformanceReview(Integer id);`
        // back to the service interface and implement it.
        // For now, we'll comment this out as it's not implemented in the service.
        // reviewService.deletePerformanceReview(id);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PerformanceReviewResponseDTO>> getReviewsByEmployee(@PathVariable Integer employeeId) {
        List<PerformanceReviewResponseDTO> reviews = reviewService.getReviewsByEmployee(employeeId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/evaluator/{evaluatorId}")
    public ResponseEntity<List<PerformanceReviewResponseDTO>> getReviewsByEvaluator(@PathVariable Integer evaluatorId) {
        List<PerformanceReviewResponseDTO> reviews = reviewService.getReviewsByEvaluator(evaluatorId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}