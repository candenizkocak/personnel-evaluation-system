package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.FeedbackCreateDTO;
import com.workin.personnelevaluationsystem.dto.FeedbackResponseDTO;
import com.workin.personnelevaluationsystem.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> createFeedback(@Valid @RequestBody FeedbackCreateDTO feedbackDTO) {
        FeedbackResponseDTO createdFeedback = feedbackService.createFeedback(feedbackDTO);
        return new ResponseEntity<>(createdFeedback, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> getFeedbackById(@PathVariable Integer id) {
        return feedbackService.getFeedbackById(id)
                .map(feedbackDTO -> new ResponseEntity<>(feedbackDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<FeedbackResponseDTO>> getAllFeedback() {
        List<FeedbackResponseDTO> feedbackList = feedbackService.getAllFeedback();
        return new ResponseEntity<>(feedbackList, HttpStatus.OK);
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbackBySenderId(@PathVariable Integer senderId) {
        List<FeedbackResponseDTO> feedbackList = feedbackService.getFeedbackBySenderId(senderId);
        return new ResponseEntity<>(feedbackList, HttpStatus.OK);
    }

    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbackByReceiverId(@PathVariable Integer receiverId) {
        List<FeedbackResponseDTO> feedbackList = feedbackService.getFeedbackByReceiverId(receiverId);
        return new ResponseEntity<>(feedbackList, HttpStatus.OK);
    }

    @GetMapping("/type/{feedbackTypeId}")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbackByTypeId(@PathVariable Integer feedbackTypeId) {
        List<FeedbackResponseDTO> feedbackList = feedbackService.getFeedbackByTypeId(feedbackTypeId);
        return new ResponseEntity<>(feedbackList, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updateFeedback(@PathVariable Integer id, @Valid @RequestBody FeedbackCreateDTO feedbackDetailsDTO) {
        FeedbackResponseDTO updatedFeedback = feedbackService.updateFeedback(id, feedbackDetailsDTO);
        return new ResponseEntity<>(updatedFeedback, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Integer id) {
        feedbackService.deleteFeedback(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}