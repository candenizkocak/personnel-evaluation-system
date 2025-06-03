package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.QuestionTypeDTO;
import com.workin.personnelevaluationsystem.service.QuestionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/question-types")
public class QuestionTypeController {

    private final QuestionTypeService questionTypeService;

    @Autowired
    public QuestionTypeController(QuestionTypeService questionTypeService) {
        this.questionTypeService = questionTypeService;
    }

    @PostMapping
    public ResponseEntity<QuestionTypeDTO> createQuestionType(@Valid @RequestBody QuestionTypeDTO questionTypeDTO) {
        QuestionTypeDTO createdQuestionType = questionTypeService.createQuestionType(questionTypeDTO);
        return new ResponseEntity<>(createdQuestionType, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionTypeDTO> getQuestionTypeById(@PathVariable Integer id) {
        return questionTypeService.getQuestionTypeById(id)
                .map(questionTypeDTO -> new ResponseEntity<>(questionTypeDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<QuestionTypeDTO>> getAllQuestionTypes() {
        List<QuestionTypeDTO> questionTypes = questionTypeService.getAllQuestionTypes();
        return new ResponseEntity<>(questionTypes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionTypeDTO> updateQuestionType(@PathVariable Integer id, @Valid @RequestBody QuestionTypeDTO questionTypeDetailsDTO) {
        QuestionTypeDTO updatedQuestionType = questionTypeService.updateQuestionType(id, questionTypeDetailsDTO);
        return new ResponseEntity<>(updatedQuestionType, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionType(@PathVariable Integer id) {
        questionTypeService.deleteQuestionType(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}