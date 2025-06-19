package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.EmployeeCompetencyDTO;
import com.workin.personnelevaluationsystem.model.User;
import com.workin.personnelevaluationsystem.service.CompetencyLevelService;
import com.workin.personnelevaluationsystem.service.CompetencyService;
import com.workin.personnelevaluationsystem.service.EmployeeCompetencyService;
import com.workin.personnelevaluationsystem.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/employee-competencies")
@PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST', 'MANAGER')")
public class EmployeeCompetencyWebController {

    private final EmployeeCompetencyService employeeCompetencyService;
    private final EmployeeService employeeService;
    private final CompetencyService competencyService;
    private final CompetencyLevelService competencyLevelService;

    @Autowired
    public EmployeeCompetencyWebController(EmployeeCompetencyService employeeCompetencyService, EmployeeService employeeService, CompetencyService competencyService, CompetencyLevelService competencyLevelService) {
        this.employeeCompetencyService = employeeCompetencyService;
        this.employeeService = employeeService;
        this.competencyService = competencyService;
        this.competencyLevelService = competencyLevelService;
    }

    private void populateFormModel(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("competencies", competencyService.getAllCompetencies());
        // For simplicity, we load all levels. A more advanced UI might load levels via JS based on competency selection.
        model.addAttribute("levels", competencyLevelService.getAllCompetencyLevels()); // You may need to add getAllCompetencyLevels() to your service
    }

    @GetMapping
    public String listAssessments(Model model) {
        model.addAttribute("assessments", employeeCompetencyService.getAllEmployeeCompetencies());
        model.addAttribute("pageTitle", "Employee Competency Assessments");
        return "employee-competencies/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model, @AuthenticationPrincipal User currentUser) {
        EmployeeCompetencyDTO dto = new EmployeeCompetencyDTO();
        dto.setAssessmentDate(LocalDate.now()); // Default to today
        if (currentUser.getEmployee() != null) {
            dto.setAssessedByID(currentUser.getEmployee().getEmployeeID()); // Default assessor to current user
        }
        model.addAttribute("assessment", dto);
        model.addAttribute("pageTitle", "Assess Employee Competency");
        populateFormModel(model);
        return "employee-competencies/form";
    }

    @PostMapping("/save")
    public String saveAssessment(@Valid @ModelAttribute("assessment") EmployeeCompetencyDTO dto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Assess Employee Competency");
            populateFormModel(model);
            return "employee-competencies/form";
        }
        try {
            employeeCompetencyService.createEmployeeCompetency(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Assessment saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving assessment: " + e.getMessage());
        }
        return "redirect:/employee-competencies";
    }

    @GetMapping("/delete/{employeeId}/{competencyId}/{assessmentDate}")
    public String deleteAssessment(@PathVariable Integer employeeId,
                                   @PathVariable Integer competencyId,
                                   @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate assessmentDate,
                                   RedirectAttributes redirectAttributes) {
        try {
            employeeCompetencyService.deleteEmployeeCompetency(employeeId, competencyId, assessmentDate);
            redirectAttributes.addFlashAttribute("successMessage", "Assessment deleted successfully.");
        } catch (Exception e) {
            // Log the actual error for administrators/developers
            System.err.println("Error deleting assessment: " + e.getMessage());
            e.printStackTrace();

            // Show user-friendly message
            redirectAttributes.addFlashAttribute("errorMessage", "Can't complete the operation as the entity is in active use.");
        }
        return "redirect:/employee-competencies";
    }
}