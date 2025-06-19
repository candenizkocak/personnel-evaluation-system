package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.EvaluationPeriodDTO;
import com.workin.personnelevaluationsystem.service.EvaluationPeriodService;
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
@RequestMapping("/evaluation-periods")
public class EvaluationPeriodWebController {

    private final EvaluationPeriodService evaluationPeriodService;

    @Autowired
    public EvaluationPeriodWebController(EvaluationPeriodService evaluationPeriodService) {
        this.evaluationPeriodService = evaluationPeriodService;
    }

    // Helper to prepare model for form
    private void prepareFormModel(Model model, EvaluationPeriodDTO periodDTO, boolean isEdit) {
        model.addAttribute("evaluationPeriod", periodDTO);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("pageTitle", isEdit ? "Edit Evaluation Period" : "Add New Evaluation Period");
    }

    // List all evaluation periods
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String listEvaluationPeriods(Model model) {
        List<EvaluationPeriodDTO> periods = evaluationPeriodService.getAllEvaluationPeriods();
        model.addAttribute("evaluationPeriods", periods);
        model.addAttribute("pageTitle", "Evaluation Periods List");
        return "evaluation-periods/list";
    }

    // Show form for new period
    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String showAddForm(Model model) {
        EvaluationPeriodDTO periodDTO = (EvaluationPeriodDTO) model.getAttribute("evaluationPeriod");
        if (periodDTO == null) {
            periodDTO = new EvaluationPeriodDTO();
            periodDTO.setIsActive(true); // Default to active for new periods
        }
        prepareFormModel(model, periodDTO, false);
        return "evaluation-periods/form";
    }

    // Show form for editing period
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        EvaluationPeriodDTO periodDTO = (EvaluationPeriodDTO) model.getAttribute("evaluationPeriod");
        if (periodDTO == null || (periodDTO.getPeriodID() != null && !periodDTO.getPeriodID().equals(id))) {
            Optional<EvaluationPeriodDTO> existingPeriod = evaluationPeriodService.getEvaluationPeriodById(id);
            if (existingPeriod.isPresent()) {
                periodDTO = existingPeriod.get();
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Evaluation Period not found!");
                return "redirect:/evaluation-periods";
            }
        }
        prepareFormModel(model, periodDTO, true);
        return "evaluation-periods/form";
    }

    // Save/Update period
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String saveEvaluationPeriod(@Valid EvaluationPeriodDTO evaluationPeriodDTO,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred. Please check the form.");
            redirectAttributes.addFlashAttribute("evaluationPeriod", evaluationPeriodDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "evaluationPeriod", bindingResult);
            return (evaluationPeriodDTO.getPeriodID() != null) ? "redirect:/evaluation-periods/edit/" + evaluationPeriodDTO.getPeriodID() : "redirect:/evaluation-periods/new";
        }

        try {
            if (evaluationPeriodDTO.getPeriodID() == null) {
                evaluationPeriodService.createEvaluationPeriod(evaluationPeriodDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Evaluation Period created successfully!");
            } else {
                evaluationPeriodService.updateEvaluationPeriod(evaluationPeriodDTO.getPeriodID(), evaluationPeriodDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Evaluation Period updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving evaluation period: " + e.getMessage());
            redirectAttributes.addFlashAttribute("evaluationPeriod", evaluationPeriodDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "evaluationPeriod", bindingResult);
            return (evaluationPeriodDTO.getPeriodID() != null) ? "redirect:/evaluation-periods/edit/" + evaluationPeriodDTO.getPeriodID() : "redirect:/evaluation-periods/new";
        }

        return "redirect:/evaluation-periods";
    }

    // Delete period
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String deleteEvaluationPeriod(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            evaluationPeriodService.deleteEvaluationPeriod(id);
            redirectAttributes.addFlashAttribute("successMessage", "Evaluation Period deleted successfully!");
        } catch (Exception e) {
            // Log the actual error for administrators/developers
            System.err.println("Error deleting evaluation period: " + e.getMessage());
            e.printStackTrace();

            // Show user-friendly message
            redirectAttributes.addFlashAttribute("errorMessage", "Can't complete the operation as the entity is in active use.");
        }
        return "redirect:/evaluation-periods";
    }
}