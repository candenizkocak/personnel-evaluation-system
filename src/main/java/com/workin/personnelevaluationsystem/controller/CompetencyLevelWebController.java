package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.CompetencyLevelDTO;
import com.workin.personnelevaluationsystem.service.CompetencyLevelService;
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
@RequestMapping("/competency-levels")
@PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
public class CompetencyLevelWebController {

    private final CompetencyLevelService levelService;
    private final CompetencyService competencyService;

    @Autowired
    public CompetencyLevelWebController(CompetencyLevelService levelService, CompetencyService competencyService) {
        this.levelService = levelService;
        this.competencyService = competencyService;
    }

    // Add this new method to handle the main competency-levels route
    @GetMapping
    public String listCompetencies(Model model) {
        model.addAttribute("competencies", competencyService.getAllCompetencies());
        model.addAttribute("pageTitle", "Competency Levels");
        return "competency-levels/list";
    }

    // Main view will be per-competency
    @GetMapping("/manage/{competencyId}")
    public String manageLevelsForCompetency(@PathVariable Integer competencyId, Model model, RedirectAttributes redirectAttributes) {
        return competencyService.getCompetencyById(competencyId).map(competency -> {
            model.addAttribute("competency", competency);
            model.addAttribute("levels", levelService.getCompetencyLevelsByCompetencyId(competencyId));
            model.addAttribute("newLevel", new CompetencyLevelDTO());
            model.addAttribute("pageTitle", "Manage Levels for: " + competency.getName());
            return "competency-levels/manage";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Competency not found.");
            return "redirect:/competencies";
        });
    }

    @PostMapping("/add/{competencyId}")
    public String addLevel(@PathVariable Integer competencyId, @Valid @ModelAttribute("newLevel") CompetencyLevelDTO levelDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // This is a tricky redirect, we pass the error message and let the user re-enter
            redirectAttributes.addFlashAttribute("errorMessage", "Validation error: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            try {
                levelService.createCompetencyLevel(competencyId, levelDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Level added successfully.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error adding level: " + e.getMessage());
            }
        }
        return "redirect:/competency-levels/manage/" + competencyId;
    }

    @GetMapping("/delete/{levelId}")
    public String deleteLevel(@PathVariable Integer levelId, @RequestParam("competencyId") Integer competencyId, RedirectAttributes redirectAttributes) {
        try {
            levelService.deleteCompetencyLevel(levelId);
            redirectAttributes.addFlashAttribute("successMessage", "Level deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting level: " + e.getMessage());
        }
        return "redirect:/competency-levels/manage/" + competencyId;
    }
}

