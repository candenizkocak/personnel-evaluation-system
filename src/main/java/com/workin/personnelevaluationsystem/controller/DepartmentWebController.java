package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.DepartmentDTO;
import com.workin.personnelevaluationsystem.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // Import
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid; // Import
import java.util.List;

@Controller
@RequestMapping("/departments")
public class DepartmentWebController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentWebController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String listDepartments(Model model) {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        model.addAttribute("pageTitle", "Departments List");
        return "departments/list";
    }

    // Show form for adding a new department
    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String showAddForm(Model model) {
        // Check if there's already a department object from a redirect (e.g., validation error)
        if (!model.containsAttribute("department")) {
            model.addAttribute("department", new DepartmentDTO()); // Provide empty DTO if not present
        }
        model.addAttribute("pageTitle", "Add New Department");
        model.addAttribute("isEdit", false);
        return "departments/form";
    }

    // Show form for editing an existing department
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        // Check if there's already a department object from a redirect (e.g., validation error)
        if (!model.containsAttribute("department")) {
            return departmentService.getDepartmentById(id)
                    .map(departmentDTO -> {
                        model.addAttribute("department", departmentDTO);
                        model.addAttribute("pageTitle", "Edit Department");
                        model.addAttribute("isEdit", true);
                        return "departments/form";
                    })
                    .orElseGet(() -> {
                        redirectAttributes.addFlashAttribute("errorMessage", "Department not found!");
                        return "redirect:/departments";
                    });
        }
        // If it came from a redirect with errors, department object is already in model, just add the flags
        model.addAttribute("pageTitle", "Edit Department");
        model.addAttribute("isEdit", true);
        return "departments/form";
    }


    // Handle submission of add/edit form
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String saveDepartment(@Valid DepartmentDTO departmentDTO,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) { // Add Model here to pass to return view

        if (bindingResult.hasErrors()) {
            // Flash attributes are good for one-time messages after redirect
            redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred. Please check the form.");
            // For retaining form data and bindingResult errors, use addFlashAttribute directly
            // Spring automatically handles passing these across redirects when a model attribute is used
            // with a BindingResult.
            redirectAttributes.addFlashAttribute("department", departmentDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "department", bindingResult);

            // Determine if it's an edit or new based on departmentID
            return (departmentDTO.getDepartmentID() != null) ? "redirect:/departments/edit/" + departmentDTO.getDepartmentID() : "redirect:/departments/new";
        }

        try {
            if (departmentDTO.getDepartmentID() == null) { // New department
                departmentService.createDepartment(departmentDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Department created successfully!");
            } else { // Existing department
                departmentService.updateDepartment(departmentDTO.getDepartmentID(), departmentDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Department updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving department: " + e.getMessage());
            // Also pass the DTO and errors back for display if an exception occurs
            redirectAttributes.addFlashAttribute("department", departmentDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "department", bindingResult); // Pass original errors if any
            return (departmentDTO.getDepartmentID() != null) ? "redirect:/departments/edit/" + departmentDTO.getDepartmentID() : "redirect:/departments/new";
        }

        return "redirect:/departments";
    }

    // Handle deletion of a department
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String deleteDepartment(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            departmentService.deleteDepartment(id);
            redirectAttributes.addFlashAttribute("successMessage", "Department deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting department: " + e.getMessage());
        }
        return "redirect:/departments";
    }
}