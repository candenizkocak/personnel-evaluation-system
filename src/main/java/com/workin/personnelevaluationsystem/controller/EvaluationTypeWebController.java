package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.EvaluationTypeDTO;
import com.workin.personnelevaluationsystem.service.EvaluationTypeService;
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
@RequestMapping("/evaluation-types")
public class EvaluationTypeWebController {

    private final EvaluationTypeService evaluationTypeService;

    @Autowired
    public EvaluationTypeWebController(EvaluationTypeService evaluationTypeService) {
        this.evaluationTypeService = evaluationTypeService;
    }

    // Helper to prepare model for form
    private void prepareFormModel(Model model, EvaluationTypeDTO typeDTO, boolean isEdit) {
        model.addAttribute("evaluationType", typeDTO);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("pageTitle", isEdit ? "Edit Evaluation Type" : "Add New Evaluation Type");
    }

    // List all evaluation types
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String listEvaluationTypes(Model model) {
        List<EvaluationTypeDTO> types = evaluationTypeService.getAllEvaluationTypes();
        model.addAttribute("evaluationTypes", types);
        model.addAttribute("pageTitle", "Evaluation Types List");
        return "evaluation-types/list";
    }

    // Show form for new type
    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String showAddForm(Model model) {
        EvaluationTypeDTO typeDTO = (EvaluationTypeDTO) model.getAttribute("evaluationType");
        if (typeDTO == null) {
            typeDTO = new EvaluationTypeDTO();
        }
        prepareFormModel(model, typeDTO, false);
        return "evaluation-types/form";
    }

    // Show form for editing type
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        EvaluationTypeDTO typeDTO = (EvaluationTypeDTO) model.getAttribute("evaluationType");
        if (typeDTO == null || (typeDTO.getTypeID() != null && !typeDTO.getTypeID().equals(id))) {
            Optional<EvaluationTypeDTO> existingType = evaluationTypeService.getEvaluationTypeById(id);
            if (existingType.isPresent()) {
                typeDTO = existingType.get();
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Evaluation Type not found!");
                return "redirect:/evaluation-types";
            }
        }
        prepareFormModel(model, typeDTO, true);
        return "evaluation-types/form";
    }

    // Save/Update type
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String saveEvaluationType(@Valid EvaluationTypeDTO evaluationTypeDTO,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred. Please check the form.");
            redirectAttributes.addFlashAttribute("evaluationType", evaluationTypeDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "evaluationType", bindingResult);
            return (evaluationTypeDTO.getTypeID() != null) ? "redirect:/evaluation-types/edit/" + evaluationTypeDTO.getTypeID() : "redirect:/evaluation-types/new";
        }

        try {
            if (evaluationTypeDTO.getTypeID() == null) {
                evaluationTypeService.createEvaluationType(evaluationTypeDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Evaluation Type created successfully!");
            } else {
                evaluationTypeService.updateEvaluationType(evaluationTypeDTO.getTypeID(), evaluationTypeDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Evaluation Type updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving evaluation type: " + e.getMessage());
            redirectAttributes.addFlashAttribute("evaluationType", evaluationTypeDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "evaluationType", bindingResult);
            return (evaluationTypeDTO.getTypeID() != null) ? "redirect:/evaluation-types/edit/" + evaluationTypeDTO.getTypeID() : "redirect:/evaluation-types/new";
        }

        return "redirect:/evaluation-types";
    }

    // Delete type
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String deleteEvaluationType(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            evaluationTypeService.deleteEvaluationType(id);
            redirectAttributes.addFlashAttribute("successMessage", "Evaluation Type deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting evaluation type: " + e.getMessage());
        }
        return "redirect:/evaluation-types";
    }
}