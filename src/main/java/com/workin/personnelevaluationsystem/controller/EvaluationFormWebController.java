package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.EvaluationFormDTO;
import com.workin.personnelevaluationsystem.dto.EvaluationQuestionDTO;
import com.workin.personnelevaluationsystem.dto.EvaluationTypeDTO;
import com.workin.personnelevaluationsystem.dto.QuestionTypeDTO;
import com.workin.personnelevaluationsystem.service.EvaluationFormService;
import com.workin.personnelevaluationsystem.service.EvaluationQuestionService;
import com.workin.personnelevaluationsystem.service.EvaluationTypeService;
import com.workin.personnelevaluationsystem.service.QuestionTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/evaluation-forms")
public class EvaluationFormWebController {

    private final EvaluationFormService evaluationFormService;
    private final EvaluationTypeService evaluationTypeService;
    private final QuestionTypeService questionTypeService;
    private final EvaluationQuestionService questionService;

    @Autowired
    public EvaluationFormWebController(EvaluationFormService evaluationFormService,
                                       EvaluationTypeService evaluationTypeService,
                                       QuestionTypeService questionTypeService,
                                       EvaluationQuestionService questionService) {
        this.evaluationFormService = evaluationFormService;
        this.evaluationTypeService = evaluationTypeService;
        this.questionTypeService = questionTypeService;
        this.questionService = questionService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST', 'MANAGER')")
    public String listEvaluationForms(Model model) {
        model.addAttribute("evaluationForms", evaluationFormService.getAllEvaluationForms());
        model.addAttribute("pageTitle", "Evaluation Forms");
        return "evaluation-forms/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String showAddForm(Model model) {
        model.addAttribute("evaluationForm", new EvaluationFormDTO());
        model.addAttribute("evaluationTypes", evaluationTypeService.getAllEvaluationTypes());
        model.addAttribute("pageTitle", "Create New Form Shell");
        return "evaluation-forms/form-shell";
    }

    @PostMapping("/save-shell")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String saveFormShell(@Valid @ModelAttribute("evaluationForm") EvaluationFormDTO formDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("evaluationTypes", evaluationTypeService.getAllEvaluationTypes());
            model.addAttribute("pageTitle", "Create New Form Shell");
            return "evaluation-forms/form-shell";
        }
        try {
            EvaluationFormDTO savedForm = evaluationFormService.createEvaluationForm(formDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Form shell created. Now add questions.");
            return "redirect:/evaluation-forms/edit/" + savedForm.getFormID();
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating form: " + e.getMessage());
            model.addAttribute("evaluationTypes", evaluationTypeService.getAllEvaluationTypes());
            model.addAttribute("pageTitle", "Create New Form Shell");
            return "evaluation-forms/form-shell";
        }
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return evaluationFormService.getEvaluationFormById(id).map(form -> {
            model.addAttribute("evaluationForm", form);
            model.addAttribute("evaluationTypes", evaluationTypeService.getAllEvaluationTypes()); // For the details form
            model.addAttribute("questionTypes", questionTypeService.getAllQuestionTypes()); // For the add question form
            model.addAttribute("newQuestion", new EvaluationQuestionDTO());
            model.addAttribute("pageTitle", "Manage Form: " + form.getTitle());
            return "evaluation-forms/form-manage-questions";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Form not found.");
            return "redirect:/evaluation-forms";
        });
    }

    // New endpoint to update the main form details
    @PostMapping("/update-details/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String updateFormDetails(@PathVariable Integer id, @Valid @ModelAttribute("evaluationForm") EvaluationFormDTO formDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // If validation fails, redirect back with the errors
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.evaluationForm", bindingResult);
            redirectAttributes.addFlashAttribute("evaluationForm", formDTO);
            return "redirect:/evaluation-forms/edit/" + id;
        }
        try {
            evaluationFormService.updateEvaluationForm(id, formDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Form details updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating details: " + e.getMessage());
        }
        return "redirect:/evaluation-forms/edit/" + id;
    }


    @PostMapping("/{formId}/questions/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String addQuestionToForm(@PathVariable Integer formId, @Valid @ModelAttribute("newQuestion") EvaluationQuestionDTO questionDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newQuestion", bindingResult);
            redirectAttributes.addFlashAttribute("newQuestion", questionDTO);
            redirectAttributes.addFlashAttribute("questionError", true);
        } else {
            try {
                questionService.createEvaluationQuestion(formId, questionDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Question added successfully!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error adding question: " + e.getMessage());
            }
        }
        return "redirect:/evaluation-forms/edit/" + formId;
    }

    @GetMapping("/{formId}/questions/{questionId}/delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String deleteQuestionFromForm(@PathVariable Integer formId, @PathVariable Integer questionId, RedirectAttributes redirectAttributes) {
        try {
            questionService.deleteEvaluationQuestion(questionId);
            redirectAttributes.addFlashAttribute("successMessage", "Question deleted successfully.");
        } catch (Exception e) {
            // Log the actual error for administrators/developers
            System.err.println("Error deleting question: " + e.getMessage());
            e.printStackTrace();

            // Show user-friendly message
            redirectAttributes.addFlashAttribute("errorMessage", "Can't complete the operation as the entity is in active use.");
        }
        return "redirect:/evaluation-forms/edit/" + formId;
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String deleteEvaluationForm(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            evaluationFormService.deleteEvaluationForm(id);
            redirectAttributes.addFlashAttribute("successMessage", "Evaluation Form deleted successfully!");
        } catch (Exception e) {
            // Log the actual error for administrators/developers
            System.err.println("Error deleting form: " + e.getMessage());
            e.printStackTrace();

            // Show user-friendly message
            redirectAttributes.addFlashAttribute("errorMessage", "Can't complete the operation as the entity is in active use.");
        }
        return "redirect:/evaluation-forms";
    }
}