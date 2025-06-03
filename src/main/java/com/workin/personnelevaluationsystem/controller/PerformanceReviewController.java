package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.PerformanceReviewCreateDTO;
import com.workin.personnelevaluationsystem.dto.PerformanceReviewResponseDTO;
import com.workin.personnelevaluationsystem.service.PerformanceReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

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

    @PutMapping("/{id}")
    public ResponseEntity<PerformanceReviewResponseDTO> updatePerformanceReview(@PathVariable Integer id, @Valid @RequestBody PerformanceReviewCreateDTO reviewDetailsDTO) {
        PerformanceReviewResponseDTO updatedReview = reviewService.updatePerformanceReview(id, reviewDetailsDTO);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformanceReview(@PathVariable Integer id) {
        reviewService.deletePerformanceReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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