package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.GoalDTO;
import com.workin.personnelevaluationsystem.model.User;
import com.workin.personnelevaluationsystem.service.EmployeeService;
import com.workin.personnelevaluationsystem.service.GoalService;
import com.workin.personnelevaluationsystem.service.GoalStatusService;
import com.workin.personnelevaluationsystem.service.GoalTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/goals")
@PreAuthorize("isAuthenticated()") // All authenticated users can access goals
public class GoalWebController {

    private final GoalService goalService;
    private final EmployeeService employeeService;
    private final GoalTypeService goalTypeService;
    private final GoalStatusService goalStatusService;

    @Autowired
    public GoalWebController(GoalService goalService, EmployeeService employeeService, GoalTypeService goalTypeService, GoalStatusService goalStatusService) {
        this.goalService = goalService;
        this.employeeService = employeeService;
        this.goalTypeService = goalTypeService;
        this.goalStatusService = goalStatusService;
    }

    private void populateFormModel(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("goalTypes", goalTypeService.getAllGoalTypes());
        model.addAttribute("goalStatuses", goalStatusService.getAllGoalStatuses());
    }

    @GetMapping
    public String listGoals(Model model, @AuthenticationPrincipal User currentUser) {
        // Admins and HR see all goals, managers/employees see their own/their team's
        // For now, we'll keep it simple: Admins/HR see all.
        if (currentUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_HR_SPECIALIST"))) {
            model.addAttribute("goals", goalService.getAllGoals());
        } else {
            // Regular employees/managers see only their own goals
            model.addAttribute("goals", goalService.getGoalsByEmployeeId(currentUser.getEmployee().getEmployeeID()));
        }
        model.addAttribute("pageTitle", "Goals");
        return "goals/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model, @AuthenticationPrincipal User currentUser) {
        GoalDTO goal = new GoalDTO();
        // Pre-select the current employee
        goal.setEmployeeID(currentUser.getEmployee().getEmployeeID());
        model.addAttribute("goal", goal);
        model.addAttribute("pageTitle", "Set New Goal");
        populateFormModel(model);
        return "goals/form";
    }

    @PostMapping("/save")
    public String saveGoal(@Valid @ModelAttribute("goal") GoalDTO goalDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", goalDTO.getGoalID() == null ? "Set New Goal" : "Edit Goal");
            populateFormModel(model);
            return "goals/form";
        }
        boolean isNew = goalDTO.getGoalID() == null;
        try {
            if (isNew) {
                goalService.createGoal(goalDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Goal created successfully!");
            } else {
                goalService.updateGoal(goalDTO.getGoalID(), goalDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Goal updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/goals";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return goalService.getGoalById(id).map(goal -> {
            model.addAttribute("goal", goal);
            model.addAttribute("pageTitle", "Edit Goal");
            populateFormModel(model);
            return "goals/form";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Goal not found.");
            return "redirect:/goals";
        });
    }

    @GetMapping("/delete/{id}")
    public String deleteGoal(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            goalService.deleteGoal(id);
            redirectAttributes.addFlashAttribute("successMessage", "Goal deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting Goal: " + e.getMessage());
        }
        return "redirect:/goals";
    }
}