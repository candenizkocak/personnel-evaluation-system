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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        model.addAttribute("competenciesWithLevels", competencyService.getAllCompetencies());
        model.addAttribute("levels", competencyLevelService.getAllCompetencyLevels());
    }

    @GetMapping
    public String listAssessments(Model model, @AuthenticationPrincipal User currentUser) {
        List<EmployeeCompetencyDTO> assessments;
        boolean isAdminOrHr = currentUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(roleName -> "ROLE_ADMIN".equals(roleName) || "ROLE_HR_SPECIALIST".equals(roleName));

        if (isAdminOrHr) {
            assessments = employeeCompetencyService.getAllEmployeeCompetencies();
        } else { // Must be MANAGER
            if (currentUser.getEmployee() != null) {
                assessments = employeeCompetencyService.getCompetenciesForSubordinates(currentUser.getEmployee().getEmployeeID());
            } else {
                assessments = new ArrayList<>();
                model.addAttribute("errorMessage", "Your user profile is not linked to an employee record. Cannot display subordinate competencies.");
            }
        }
        model.addAttribute("assessments", assessments);
        model.addAttribute("pageTitle", "Employee Competency Assessments");
        return "employee-competencies/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model, @AuthenticationPrincipal User currentUser) {
        EmployeeCompetencyDTO dto = new EmployeeCompetencyDTO();
        dto.setAssessmentDate(LocalDate.now());

        if (currentUser != null && currentUser.getEmployee() != null) {
            dto.setAssessedByID(currentUser.getEmployee().getEmployeeID());
            // Pass the current assessor's details to the view
            employeeService.getEmployeeById(currentUser.getEmployee().getEmployeeID())
                    .ifPresent(assessor -> model.addAttribute("currentAssessor", assessor));
        } else {
            // Handle case where current user or their employee link is null
            // This might happen if an Admin user doesn't have an Employee record
            // In such a case, allow selection or show an error/different UI
            model.addAttribute("allowAssessorSelection", true); // Or some other flag
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