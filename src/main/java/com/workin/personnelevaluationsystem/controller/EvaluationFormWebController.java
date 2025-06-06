package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.EvaluationFormDTO;
import com.workin.personnelevaluationsystem.dto.EvaluationQuestionDTO; // Nested DTO
import com.workin.personnelevaluationsystem.dto.EvaluationTypeDTO; // For dropdown
import com.workin.personnelevaluationsystem.dto.QuestionTypeDTO; // For nested dropdown
import com.workin.personnelevaluationsystem.service.EvaluationFormService;
import com.workin.personnelevaluationsystem.service.EvaluationTypeService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/evaluation-forms")
public class EvaluationFormWebController {

    private final EvaluationFormService evaluationFormService;
    private final EvaluationTypeService evaluationTypeService;
    private final QuestionTypeService questionTypeService;

    @Autowired
    public EvaluationFormWebController(EvaluationFormService evaluationFormService,
                                       EvaluationTypeService evaluationTypeService,
                                       QuestionTypeService questionTypeService) {
        this.evaluationFormService = evaluationFormService;
        this.evaluationTypeService = evaluationTypeService;
        this.questionTypeService = questionTypeService;
    }

    // Helper to prepare model for form
    private void prepareFormModel(Model model, EvaluationFormDTO formDTO, boolean isEdit) {
        model.addAttribute("evaluationForm", formDTO);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("pageTitle", isEdit ? "Edit Evaluation Form" : "Create New Evaluation Form");

        // Fetch all evaluation types for the dropdown
        List<EvaluationTypeDTO> evaluationTypes = evaluationTypeService.getAllEvaluationTypes();
        model.addAttribute("evaluationTypes", evaluationTypes);

        // Fetch all question types for the nested question dropdowns
        List<QuestionTypeDTO> questionTypes = questionTypeService.getAllQuestionTypes();
        model.addAttribute("questionTypes", questionTypes);
    }

    // List all evaluation forms
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST', 'MANAGER')") // Managers might need to view forms too
    public String listEvaluationForms(Model model) {
        List<EvaluationFormDTO> forms = evaluationFormService.getAllEvaluationForms();
        model.addAttribute("evaluationForms", forms);
        model.addAttribute("pageTitle", "Evaluation Forms List");
        return "evaluation-forms/list";
    }

    // Show form for new evaluation form
    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String showAddForm(Model model) {
        EvaluationFormDTO formDTO = (EvaluationFormDTO) model.getAttribute("evaluationForm");
        if (formDTO == null) {
            formDTO = new EvaluationFormDTO();
            formDTO.setIsActive(true); // Default to active
            formDTO.setQuestions(new ArrayList<>()); // Initialize questions list for new form
        }
        prepareFormModel(model, formDTO, false);
        return "evaluation-forms/form";
    }

    // Show form for editing evaluation form
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        EvaluationFormDTO formDTO = (EvaluationFormDTO) model.getAttribute("evaluationForm");
        if (formDTO == null || (formDTO.getFormID() != null && !formDTO.getFormID().equals(id))) {
            Optional<EvaluationFormDTO> existingForm = evaluationFormService.getEvaluationFormById(id);
            if (existingForm.isPresent()) {
                formDTO = existingForm.get();
                // Ensure questions list is not null, for adding new questions on edit
                if (formDTO.getQuestions() == null) {
                    formDTO.setQuestions(new ArrayList<>());
                }
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Evaluation Form not found!");
                return "redirect:/evaluation-forms";
            }
        }
        prepareFormModel(model, formDTO, true);
        return "evaluation-forms/form";
    }

    // Save/Update evaluation form
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String saveEvaluationForm(@Valid EvaluationFormDTO evaluationFormDTO,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {

        // Custom validation for questions list (e.g., minimum number of questions)
        if (evaluationFormDTO.getQuestions() == null || evaluationFormDTO.getQuestions().isEmpty()) {
            // bindingResult.rejectValue("questions", "NotEmpty.evaluationForm.questions", "An evaluation form must have at least one question.");
            // Or, handle as a global error
            if (evaluationFormDTO.getQuestions() == null || evaluationFormDTO.getQuestions().isEmpty()) {
                bindingResult.reject("form.questions.empty", "An evaluation form must have at least one question.");
            }
        } else {
            // Validate uniqueness of orderIndex within the form
            long distinctOrderIndexes = evaluationFormDTO.getQuestions().stream()
                    .map(EvaluationQuestionDTO::getOrderIndex)
                    .distinct()
                    .count();
            if (distinctOrderIndexes != evaluationFormDTO.getQuestions().size()) {
                bindingResult.reject("form.questions.duplicateOrderIndex", "Each question must have a unique order index within the form.");
            }
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred. Please check the form.");
            redirectAttributes.addFlashAttribute("evaluationForm", evaluationFormDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "evaluationForm", bindingResult);
            return (evaluationFormDTO.getFormID() != null) ? "redirect:/evaluation-forms/edit/" + evaluationFormDTO.getFormID() : "redirect:/evaluation-forms/new";
        }

        try {
            if (evaluationFormDTO.getFormID() == null) {
                evaluationFormService.createEvaluationForm(evaluationFormDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Evaluation Form created successfully!");
            } else {
                evaluationFormService.updateEvaluationForm(evaluationFormDTO.getFormID(), evaluationFormDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Evaluation Form updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving evaluation form: " + e.getMessage());
            redirectAttributes.addFlashAttribute("evaluationForm", evaluationFormDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "evaluationForm", bindingResult);
            return (evaluationFormDTO.getFormID() != null) ? "redirect:/evaluation-forms/edit/" + evaluationFormDTO.getFormID() : "redirect:/evaluation-forms/new";
        }

        return "redirect:/evaluation-forms";
    }

    // Delete evaluation form
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String deleteEvaluationForm(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            evaluationFormService.deleteEvaluationForm(id);
            redirectAttributes.addFlashAttribute("successMessage", "Evaluation Form deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting evaluation form: " + e.getMessage());
        }
        return "redirect:/evaluation-forms";
    }
}