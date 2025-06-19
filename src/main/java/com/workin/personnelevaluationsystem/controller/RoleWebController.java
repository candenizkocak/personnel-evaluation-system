package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.PermissionDTO; // For dropdown/checkboxes
import com.workin.personnelevaluationsystem.dto.RoleDTO; // For Role CRUD
import com.workin.personnelevaluationsystem.service.PermissionService; // For fetching permissions
import com.workin.personnelevaluationsystem.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/roles") // Base URL for Role UI pages
public class RoleWebController {

    private final RoleService roleService;
    private final PermissionService permissionService; // For fetching permissions for selection

    @Autowired
    public RoleWebController(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    // Helper to prepare model for role form (reusable for new and edit)
    private void prepareRoleFormModel(Model model, RoleDTO roleDTO, boolean isEdit) {
        model.addAttribute("role", roleDTO);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("pageTitle", isEdit ? "Edit Role" : "Add New Role");

        // Fetch all permissions for the multi-select/checkboxes
        List<PermissionDTO> allPermissions = permissionService.getAllPermissions();
        model.addAttribute("allPermissions", allPermissions);
    }

    // Display list of roles
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Only ADMIN can view/manage roles
    public String listRoles(Model model) {
        List<RoleDTO> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("pageTitle", "Roles List");
        return "roles/list"; // Resolves to /WEB-INF/views/roles/list.jsp
    }

    // Show form for adding a new role
    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddForm(Model model) {
        RoleDTO roleDTO = (RoleDTO) model.getAttribute("role");
        if (roleDTO == null) {
            roleDTO = new RoleDTO(); // Provide empty DTO if not present from redirect
        }
        prepareRoleFormModel(model, roleDTO, false);
        return "roles/form"; // Resolves to /WEB-INF/views/roles/form.jsp
    }

    // Show form for editing an existing role
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        RoleDTO roleDTO = (RoleDTO) model.getAttribute("role");
        if (roleDTO == null || (roleDTO.getRoleID() != null && !roleDTO.getRoleID().equals(id))) {
            Optional<RoleDTO> existingRole = roleService.getRoleById(id);
            if (existingRole.isPresent()) {
                roleDTO = existingRole.get();
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Role not found!");
                return "redirect:/roles";
            }
        }
        prepareRoleFormModel(model, roleDTO, true);
        return "roles/form";
    }

    // Handle submission of add/edit form
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveRole(@Valid RoleDTO roleDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred. Please check the form.");
            redirectAttributes.addFlashAttribute("role", roleDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "role", bindingResult);
            return (roleDTO.getRoleID() != null) ? "redirect:/roles/edit/" + roleDTO.getRoleID() : "redirect:/roles/new";
        }

        try {
            if (roleDTO.getRoleID() == null) { // New role
                roleService.createRole(roleDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Role created successfully!");
            } else { // Existing role
                roleService.updateRole(roleDTO.getRoleID(), roleDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Role updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving role: " + e.getMessage());
            // Pass back DTO and errors to repopulate form after service exception
            redirectAttributes.addFlashAttribute("role", roleDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "role", bindingResult);
            return (roleDTO.getRoleID() != null) ? "redirect:/roles/edit/" + roleDTO.getRoleID() : "redirect:/roles/new";
        }

        return "redirect:/roles";
    }

    // Handle deletion of a role
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteRole(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            roleService.deleteRole(id);
            redirectAttributes.addFlashAttribute("successMessage", "Role deleted successfully!");
        } catch (Exception e) {
            // Log the actual error for administrators/developers
            System.err.println("Error deleting role: " + e.getMessage());
            e.printStackTrace();

            // Show user-friendly message
            redirectAttributes.addFlashAttribute("errorMessage", "Can't complete the operation as the entity is in active use.");
        }
        return "redirect:/roles";
    }
}