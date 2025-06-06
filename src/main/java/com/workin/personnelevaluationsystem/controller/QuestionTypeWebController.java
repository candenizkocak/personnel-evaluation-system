package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.QuestionTypeDTO;
import com.workin.personnelevaluationsystem.service.QuestionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/question-types")
public class QuestionTypeWebController {

    private final QuestionTypeService questionTypeService;

    @Autowired
    public QuestionTypeWebController(QuestionTypeService questionTypeService) {
        this.questionTypeService = questionTypeService;
    }

    // Helper to prepare model for form
    private void prepareFormModel(Model model, QuestionTypeDTO typeDTO, boolean isEdit) {
        model.addAttribute("questionType", typeDTO);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("pageTitle", isEdit ? "Edit Question Type" : "Add New Question Type");
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

    // Show form for new type
    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String showAddForm(Model model) {
        QuestionTypeDTO typeDTO = (QuestionTypeDTO) model.getAttribute("questionType");
        if (typeDTO == null) {
            typeDTO = new QuestionTypeDTO();
        }
        prepareFormModel(model, typeDTO, false);
        return "question-types/form";
    }

    // Show form for editing type
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        QuestionTypeDTO typeDTO = (QuestionTypeDTO) model.getAttribute("questionType");
        if (typeDTO == null || (typeDTO.getQuestionTypeID() != null && !typeDTO.getQuestionTypeID().equals(id))) {
            Optional<QuestionTypeDTO> existingType = questionTypeService.getQuestionTypeById(id);
            if (existingType.isPresent()) {
                typeDTO = existingType.get();
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Question Type not found!");
                return "redirect:/question-types";
            }
        }
        prepareFormModel(model, typeDTO, true);
        return "question-types/form";
    }

    // Save/Update type
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String saveQuestionType(@Valid QuestionTypeDTO questionTypeDTO,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred. Please check the form.");
            redirectAttributes.addFlashAttribute("questionType", questionTypeDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "questionType", bindingResult);
            return (questionTypeDTO.getQuestionTypeID() != null) ? "redirect:/question-types/edit/" + questionTypeDTO.getQuestionTypeID() : "redirect:/question-types/new";
        }

        try {
            if (questionTypeDTO.getQuestionTypeID() == null) {
                questionTypeService.createQuestionType(questionTypeDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Question Type created successfully!");
            } else {
                questionTypeService.updateQuestionType(questionTypeDTO.getQuestionTypeID(), questionTypeDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Question Type updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving question type: " + e.getMessage());
            redirectAttributes.addFlashAttribute("questionType", questionTypeDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "questionType", bindingResult);
            return (questionTypeDTO.getQuestionTypeID() != null) ? "redirect:/question-types/edit/" + questionTypeDTO.getQuestionTypeID() : "redirect:/question-types/new";
        }

        return "redirect:/question-types";
    }

    // Delete type
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String deleteQuestionType(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            questionTypeService.deleteQuestionType(id);
            redirectAttributes.addFlashAttribute("successMessage", "Question Type deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting question type: " + e.getMessage());
        }
        return "redirect:/question-types";
    }
}