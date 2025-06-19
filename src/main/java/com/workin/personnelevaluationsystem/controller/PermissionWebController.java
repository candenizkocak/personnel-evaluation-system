package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.PermissionDTO; // For Permission CRUD
import com.workin.personnelevaluationsystem.service.PermissionService;
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
@RequestMapping("/permissions") // Base URL for Permission UI pages
public class PermissionWebController {

    private final PermissionService permissionService;

    @Autowired
    public PermissionWebController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    // Helper to prepare model for permission form (reusable for new and edit)
    private void preparePermissionFormModel(Model model, PermissionDTO permissionDTO, boolean isEdit) {
        model.addAttribute("permission", permissionDTO);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("pageTitle", isEdit ? "Edit Permission" : "Add New Permission");
    }

    // Display list of permissions
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Only ADMIN can view/manage permissions
    public String listPermissions(Model model) {
        List<PermissionDTO> permissions = permissionService.getAllPermissions();
        model.addAttribute("permissions", permissions);
        model.addAttribute("pageTitle", "Permissions List");
        return "permissions/list"; // Resolves to /WEB-INF/views/permissions/list.jsp
    }

    // Show form for adding a new permission
    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddForm(Model model) {
        PermissionDTO permissionDTO = (PermissionDTO) model.getAttribute("permission");
        if (permissionDTO == null) {
            permissionDTO = new PermissionDTO(); // Provide empty DTO if not present from redirect
        }
        preparePermissionFormModel(model, permissionDTO, false);
        return "permissions/form"; // Resolves to /WEB-INF/views/permissions/form.jsp
    }

    // Show form for editing an existing permission
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        PermissionDTO permissionDTO = (PermissionDTO) model.getAttribute("permission");
        if (permissionDTO == null || (permissionDTO.getPermissionID() != null && !permissionDTO.getPermissionID().equals(id))) {
            Optional<PermissionDTO> existingPermission = permissionService.getPermissionById(id);
            if (existingPermission.isPresent()) {
                permissionDTO = existingPermission.get();
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Permission not found!");
                return "redirect:/permissions";
            }
        }
        preparePermissionFormModel(model, permissionDTO, true);
        return "permissions/form";
    }

    // Handle submission of add/edit form
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String savePermission(@Valid PermissionDTO permissionDTO,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred. Please check the form.");
            redirectAttributes.addFlashAttribute("permission", permissionDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "permission", bindingResult);
            return (permissionDTO.getPermissionID() != null) ? "redirect:/permissions/edit/" + permissionDTO.getPermissionID() : "redirect:/permissions/new";
        }

        try {
            if (permissionDTO.getPermissionID() == null) { // New permission
                permissionService.createPermission(permissionDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Permission created successfully!");
            } else { // Existing permission
                permissionService.updatePermission(permissionDTO.getPermissionID(), permissionDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Permission updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving permission: " + e.getMessage());
            // Pass back DTO and errors to repopulate form after service exception
            redirectAttributes.addFlashAttribute("permission", permissionDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "permission", bindingResult);
            return (permissionDTO.getPermissionID() != null) ? "redirect:/permissions/edit/" + permissionDTO.getPermissionID() : "redirect:/permissions/new";
        }

        return "redirect:/permissions";
    }

    // Handle deletion of a permission
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deletePermission(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            permissionService.deletePermission(id);
            redirectAttributes.addFlashAttribute("successMessage", "Permission deleted successfully!");
        } catch (Exception e) {
            // Log the actual error for administrators/developers
            System.err.println("Error deleting permission: " + e.getMessage());
            e.printStackTrace();

            // Show user-friendly message
            redirectAttributes.addFlashAttribute("errorMessage", "Can't complete the operation as the entity is in active use.");
        }
        return "redirect:/permissions";
    }
}