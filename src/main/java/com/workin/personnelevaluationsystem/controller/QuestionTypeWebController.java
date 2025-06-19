package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.QuestionTypeDTO;
import com.workin.personnelevaluationsystem.service.QuestionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/question-types")
public class QuestionTypeWebController {

    private final QuestionTypeService questionTypeService;

    @Autowired
    public QuestionTypeWebController(QuestionTypeService questionTypeService) {
        this.questionTypeService = questionTypeService;
    }

    // List all question types
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String listQuestionTypes(Model model) {
        List<QuestionTypeDTO> types = questionTypeService.getAllQuestionTypes();
        model.addAttribute("questionTypes", types);
        model.addAttribute("pageTitle", "Question Types List");
        return "question-types/list";
    }

}