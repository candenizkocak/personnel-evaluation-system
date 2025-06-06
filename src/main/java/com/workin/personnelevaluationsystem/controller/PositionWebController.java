package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.PositionDTO; // Use PositionDTO for CRUD
import com.workin.personnelevaluationsystem.dto.DepartmentDTO; // Need DepartmentDTO for dropdown
import com.workin.personnelevaluationsystem.service.PositionService;
import com.workin.personnelevaluationsystem.service.DepartmentService; // Need Department Service for dropdown
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize; // For method-level security
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
@RequestMapping("/positions") // Base URL for Position UI pages
public class PositionWebController {

    private final PositionService positionService;
    private final DepartmentService departmentService; // For fetching departments for dropdown

    @Autowired
    public PositionWebController(PositionService positionService, DepartmentService departmentService) {
        this.positionService = positionService;
        this.departmentService = departmentService;
    }

    // Helper to prepare model for position form (reusable for new and edit)
    private void preparePositionFormModel(Model model, PositionDTO positionDTO, boolean isEdit) {
        model.addAttribute("position", positionDTO);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("pageTitle", isEdit ? "Edit Position" : "Add New Position");

        // Fetch all departments for the dropdown
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
    }

    // Display list of positions
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST', 'MANAGER')") // Admins, HR, Managers can view positions
    public String listPositions(Model model) {
        List<PositionDTO> positions = positionService.getAllPositions();
        model.addAttribute("positions", positions);
        model.addAttribute("pageTitle", "Positions List");
        return "positions/list"; // Resolves to /WEB-INF/views/positions/list.jsp
    }

    // Show form for adding a new position
    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')") // Only ADMIN or HR_SPECIALIST can add positions
    public String showAddForm(Model model) {
        PositionDTO positionDTO = (PositionDTO) model.getAttribute("position");
        if (positionDTO == null) {
            positionDTO = new PositionDTO(); // Provide empty DTO if not present from redirect
        }
        preparePositionFormModel(model, positionDTO, false);
        return "positions/form"; // Resolves to /WEB-INF/views/positions/form.jsp
    }

    // Show form for editing an existing position
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')") // Only ADMIN or HR_SPECIALIST can edit positions
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        PositionDTO positionDTO = (PositionDTO) model.getAttribute("position");
        if (positionDTO == null || (positionDTO.getPositionID() != null && !positionDTO.getPositionID().equals(id))) {
            Optional<PositionDTO> existingPosition = positionService.getPositionById(id);
            if (existingPosition.isPresent()) {
                positionDTO = existingPosition.get();
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Position not found!");
                return "redirect:/positions";
            }
        }
        preparePositionFormModel(model, positionDTO, true);
        return "positions/form";
    }

    // Handle submission of add/edit form
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')") // Only ADMIN or HR_SPECIALIST can save positions
    public String savePosition(@Valid PositionDTO positionDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred. Please check the form.");
            redirectAttributes.addFlashAttribute("position", positionDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "position", bindingResult);
            return (positionDTO.getPositionID() != null) ? "redirect:/positions/edit/" + positionDTO.getPositionID() : "redirect:/positions/new";
        }

        try {
            if (positionDTO.getPositionID() == null) { // New position
                positionService.createPosition(positionDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Position created successfully!");
            } else { // Existing position
                positionService.updatePosition(positionDTO.getPositionID(), positionDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Position updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving position: " + e.getMessage());
            // Pass back DTO and errors to repopulate form after service exception
            redirectAttributes.addFlashAttribute("position", positionDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "position", bindingResult);
            return (positionDTO.getPositionID() != null) ? "redirect:/positions/edit/" + positionDTO.getPositionID() : "redirect:/positions/new";
        }

        return "redirect:/positions";
    }

    // Handle deletion of a position
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')") // Only ADMIN or HR_SPECIALIST can delete positions
    public String deletePosition(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            positionService.deletePosition(id);
            redirectAttributes.addFlashAttribute("successMessage", "Position deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting position: " + e.getMessage());
        }
        return "redirect:/positions";
    }
}