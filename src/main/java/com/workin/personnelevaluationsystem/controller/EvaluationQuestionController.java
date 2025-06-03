package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.EvaluationQuestionDTO;
import com.workin.personnelevaluationsystem.service.EvaluationQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/evaluation-questions")
public class EvaluationQuestionController {

    private final EvaluationQuestionService questionService;

    @Autowired
    public EvaluationQuestionController(EvaluationQuestionService questionService) {
        this.questionService = questionService;
    }

    // Endpoint for creating a question for a specific form (nested path)
    @PostMapping("/form/{formId}")
    public ResponseEntity<EvaluationQuestionDTO> createEvaluationQuestionForForm(
            @PathVariable Integer formId,
            @Valid @RequestBody EvaluationQuestionDTO questionDTO) {
        EvaluationQuestionDTO createdQuestion = questionService.createEvaluationQuestion(formId, questionDTO);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationQuestionDTO> getEvaluationQuestionById(@PathVariable Integer id) {
        return questionService.getEvaluationQuestionById(id)
                .map(questionDTO -> new ResponseEntity<>(questionDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/form/{formId}")
    public ResponseEntity<List<EvaluationQuestionDTO>> getAllEvaluationQuestionsByFormId(@PathVariable Integer formId) {
        List<EvaluationQuestionDTO> questions = questionService.getAllEvaluationQuestionsByFormId(formId);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluationQuestionDTO> updateEvaluationQuestion(@PathVariable Integer id, @Valid @RequestBody EvaluationQuestionDTO questionDetailsDTO) {
        EvaluationQuestionDTO updatedQuestion = questionService.updateEvaluationQuestion(id, questionDetailsDTO);
        return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluationQuestion(@PathVariable Integer id) {
        questionService.deleteEvaluationQuestion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}