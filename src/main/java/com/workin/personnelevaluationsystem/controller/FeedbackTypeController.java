package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.FeedbackTypeDTO;
import com.workin.personnelevaluationsystem.service.FeedbackTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedback-types")
public class FeedbackTypeController {

    private final FeedbackTypeService feedbackTypeService;

    @Autowired
    public FeedbackTypeController(FeedbackTypeService feedbackTypeService) {
        this.feedbackTypeService = feedbackTypeService;
    }

    @PostMapping
    public ResponseEntity<FeedbackTypeDTO> createFeedbackType(@Valid @RequestBody FeedbackTypeDTO typeDTO) {
        FeedbackTypeDTO createdType = feedbackTypeService.createFeedbackType(typeDTO);
        return new ResponseEntity<>(createdType, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackTypeDTO> getFeedbackTypeById(@PathVariable Integer id) {
        return feedbackTypeService.getFeedbackTypeById(id)
                .map(typeDTO -> new ResponseEntity<>(typeDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<FeedbackTypeDTO>> getAllFeedbackTypes() {
        List<FeedbackTypeDTO> types = feedbackTypeService.getAllFeedbackTypes();
        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeedbackTypeDTO> updateFeedbackType(@PathVariable Integer id, @Valid @RequestBody FeedbackTypeDTO typeDetailsDTO) {
        FeedbackTypeDTO updatedType = feedbackTypeService.updateFeedbackType(id, typeDetailsDTO);
        return new ResponseEntity<>(updatedType, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedbackType(@PathVariable Integer id) {
        feedbackTypeService.deleteFeedbackType(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}