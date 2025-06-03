package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.ReviewResponseDTO;
import com.workin.personnelevaluationsystem.service.ReviewResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review-responses")
public class ReviewResponseController {

    private final ReviewResponseService responseService;

    @Autowired
    public ReviewResponseController(ReviewResponseService responseService) {
        this.responseService = responseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewResponseById(@PathVariable Integer id) {
        return responseService.getReviewResponseById(id)
                .map(responseDTO -> new ResponseEntity<>(responseDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/review/{reviewId}")
    public ResponseEntity<List<ReviewResponseDTO>> getResponsesByReviewId(@PathVariable Integer reviewId) {
        List<ReviewResponseDTO> responses = responseService.getResponsesByReviewId(reviewId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewResponse(@PathVariable Integer id) {
        responseService.deleteReviewResponse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}