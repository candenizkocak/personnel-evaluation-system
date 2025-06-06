package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.CompetencyDTO;
import com.workin.personnelevaluationsystem.service.CompetencyCategoryService;
import com.workin.personnelevaluationsystem.service.CompetencyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/competencies")
@PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
public class CompetencyWebController {

    private final CompetencyService competencyService;
    private final CompetencyCategoryService categoryService;

    @Autowired
    public CompetencyWebController(CompetencyService competencyService, CompetencyCategoryService categoryService) {
        this.competencyService = competencyService;
        this.categoryService = categoryService;
    }

    private void populateFormModel(Model model) {
        model.addAttribute("categories", categoryService.getAllCompetencyCategories());
    }

    @GetMapping
    public String listCompetencies(Model model) {
        model.addAttribute("competencies", competencyService.getAllCompetencies());
        model.addAttribute("pageTitle", "Competencies");
        return "competencies/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("competency", new CompetencyDTO());
        model.addAttribute("pageTitle", "Add New Competency");
        populateFormModel(model);
        return "competencies/form";
    }

    @PostMapping("/save")
    public String saveCompetency(@Valid @ModelAttribute("competency") CompetencyDTO competencyDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", competencyDTO.getCompetencyID() == null ? "Add New Competency" : "Edit Competency");
            populateFormModel(model);
            return "competencies/form";
        }
        boolean isNew = competencyDTO.getCompetencyID() == null;
        try {
            if (isNew) {
                competencyService.createCompetency(competencyDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Competency created successfully!");
            } else {
                competencyService.updateCompetency(competencyDTO.getCompetencyID(), competencyDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Competency updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/competencies";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return competencyService.getCompetencyById(id).map(competency -> {
            model.addAttribute("competency", competency);
            model.addAttribute("pageTitle", "Edit Competency");
            populateFormModel(model);
            return "competencies/form";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Competency not found.");
            return "redirect:/competencies";
        });
    }

    @GetMapping("/delete/{id}")
    public String deleteCompetency(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            competencyService.deleteCompetency(id);
            redirectAttributes.addFlashAttribute("successMessage", "Competency deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting competency: " + e.getMessage());
        }
        return "redirect:/competencies";
    }
}