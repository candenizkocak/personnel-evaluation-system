package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.EvaluationQuestionDTO;
import com.workin.personnelevaluationsystem.service.EvaluationQuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// Change to @Controller since it will now handle form posts and redirects
@Controller
@RequestMapping("/evaluation-questions")
public class EvaluationQuestionController {

    private final EvaluationQuestionService questionService;

    @Autowired
    public EvaluationQuestionController(EvaluationQuestionService questionService) {
        this.questionService = questionService;
    }

    // This endpoint is for the new update functionality
    @PostMapping("/update/{questionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String updateQuestion(@PathVariable Integer questionId,
                                 @RequestParam("formId") Integer formId, // Get formId from a hidden input
                                 @Valid @ModelAttribute("question") EvaluationQuestionDTO questionDTO,
                                 RedirectAttributes redirectAttributes) {
        try {
            questionService.updateEvaluationQuestion(questionId, questionDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Question updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating question: " + e.getMessage());
        }
        return "redirect:/evaluation-forms/edit/" + formId;
    }

    // --- The following methods can be kept for potential API use or removed if not needed ---
    // For simplicity, we will keep them as a RESTful API part of the controller.

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<EvaluationQuestionDTO> getEvaluationQuestionById(@PathVariable Integer id) {
        return questionService.getEvaluationQuestionById(id)
                .map(questionDTO -> new ResponseEntity<>(questionDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/form/{formId}")
    @ResponseBody
    public ResponseEntity<List<EvaluationQuestionDTO>> getAllEvaluationQuestionsByFormId(@PathVariable Integer formId) {
        List<EvaluationQuestionDTO> questions = questionService.getAllEvaluationQuestionsByFormId(formId);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }
}