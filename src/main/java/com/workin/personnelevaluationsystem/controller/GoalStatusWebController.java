package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.GoalStatusDTO;
import com.workin.personnelevaluationsystem.service.GoalStatusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/goal-statuses")
@PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')") // Only Admins/HR can manage these
public class GoalStatusWebController {

    private final GoalStatusService goalStatusService;

    @Autowired
    public GoalStatusWebController(GoalStatusService goalStatusService) {
        this.goalStatusService = goalStatusService;
    }

    @GetMapping
    public String listGoalStatuses(Model model) {
        model.addAttribute("goalStatuses", goalStatusService.getAllGoalStatuses());
        model.addAttribute("pageTitle", "Goal Statuses");
        return "goal-statuses/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("goalStatus", new GoalStatusDTO());
        model.addAttribute("pageTitle", "Add New Goal Status");
        return "goal-statuses/form";
    }

    @PostMapping("/save")
    public String saveGoalStatus(@Valid @ModelAttribute("goalStatus") GoalStatusDTO goalStatusDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", goalStatusDTO.getStatusID() == null ? "Add New Goal Status" : "Edit Goal Status");
            return "goal-statuses/form";
        }
        boolean isNew = goalStatusDTO.getStatusID() == null;
        try {
            if (isNew) {
                goalStatusService.createGoalStatus(goalStatusDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Goal Status created successfully!");
            } else {
                goalStatusService.updateGoalStatus(goalStatusDTO.getStatusID(), goalStatusDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Goal Status updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            if (!isNew) {
                return "redirect:/goal-statuses/edit/" + goalStatusDTO.getStatusID();
            }
            return "redirect:/goal-statuses/new";
        }
        return "redirect:/goal-statuses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return goalStatusService.getGoalStatusById(id).map(goalStatus -> {
            model.addAttribute("goalStatus", goalStatus);
            model.addAttribute("pageTitle", "Edit Goal Status");
            return "goal-statuses/form";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Goal Status not found.");
            return "redirect:/goal-statuses";
        });
    }

    @GetMapping("/delete/{id}")
    public String deleteGoalStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            goalStatusService.deleteGoalStatus(id);
            redirectAttributes.addFlashAttribute("successMessage", "Goal Status deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting Goal Status: " + e.getMessage());
        }
        return "redirect:/goal-statuses";
    }
}