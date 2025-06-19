package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.GoalTypeDTO;
import com.workin.personnelevaluationsystem.service.GoalTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/goal-types")
@PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')") // Only Admins/HR can manage these
public class GoalTypeWebController {

    private final GoalTypeService goalTypeService;

    @Autowired
    public GoalTypeWebController(GoalTypeService goalTypeService) {
        this.goalTypeService = goalTypeService;
    }

    @GetMapping
    public String listGoalTypes(Model model) {
        model.addAttribute("goalTypes", goalTypeService.getAllGoalTypes());
        model.addAttribute("pageTitle", "Goal Types");
        return "goal-types/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("goalType", new GoalTypeDTO());
        model.addAttribute("pageTitle", "Add New Goal Type");
        return "goal-types/form";
    }

    @PostMapping("/save")
    public String saveGoalType(@Valid @ModelAttribute("goalType") GoalTypeDTO goalTypeDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", goalTypeDTO.getGoalTypeID() == null ? "Add New Goal Type" : "Edit Goal Type");
            return "goal-types/form";
        }
        boolean isNew = goalTypeDTO.getGoalTypeID() == null;
        try {
            if (isNew) {
                goalTypeService.createGoalType(goalTypeDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Goal Type created successfully!");
            } else {
                goalTypeService.updateGoalType(goalTypeDTO.getGoalTypeID(), goalTypeDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Goal Type updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            if (!isNew) {
                return "redirect:/goal-types/edit/" + goalTypeDTO.getGoalTypeID();
            }
            return "redirect:/goal-types/new";
        }
        return "redirect:/goal-types";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return goalTypeService.getGoalTypeById(id).map(goalType -> {
            model.addAttribute("goalType", goalType);
            model.addAttribute("pageTitle", "Edit Goal Type");
            return "goal-types/form";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Goal Type not found.");
            return "redirect:/goal-types";
        });
    }

    @GetMapping("/delete/{id}")
    public String deleteGoalType(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            goalTypeService.deleteGoalType(id);
            redirectAttributes.addFlashAttribute("successMessage", "Goal Type deleted successfully.");
        } catch (Exception e) {
            // Log the actual error for administrators/developers
            System.err.println("Error deleting Goal Type: " + e.getMessage());
            e.printStackTrace();

            // Show user-friendly message
            redirectAttributes.addFlashAttribute("errorMessage", "Can't complete the operation as the entity is in active use.");
        }
        return "redirect:/goal-types";
    }
}