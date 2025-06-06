package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.EmployeeResponseDTO; // For employee dropdown
import com.workin.personnelevaluationsystem.dto.RoleDTO; // For role checkboxes
import com.workin.personnelevaluationsystem.dto.UserCreateDTO; // For form input
import com.workin.personnelevaluationsystem.dto.UserResponseDTO; // For list display
import com.workin.personnelevaluationsystem.service.EmployeeService;
import com.workin.personnelevaluationsystem.service.RoleService;
import com.workin.personnelevaluationsystem.service.UserService;
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
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users") // Base URL for User UI pages
public class UserWebController {

    private final UserService userService;
    private final EmployeeService employeeService; // For employee dropdown
    private final RoleService roleService; // For role checkboxes

    @Autowired
    public UserWebController(UserService userService, EmployeeService employeeService, RoleService roleService) {
        this.userService = userService;
        this.employeeService = employeeService;
        this.roleService = roleService;
    }

    // Helper to prepare model for user form (reusable for new and edit)
    private void prepareUserFormModel(Model model, UserCreateDTO userDTO, boolean isEdit) {
        model.addAttribute("user", userDTO);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("pageTitle", isEdit ? "Edit User" : "Add New User");

        // Fetch all employees for the dropdown (users can only be linked to unlinked employees)
        List<EmployeeResponseDTO> allEmployees = employeeService.getAllEmployees();
        // Filter out employees already associated with a user, unless it's the current user being edited
        List<EmployeeResponseDTO> availableEmployees = allEmployees.stream()
                .filter(emp -> {
                    // Check if an employee is linked to any user
                    Optional<UserResponseDTO> existingUserForEmployee = userService.getUserByUsername(emp.getEmail()); // Using email as a proxy for username if not linked
                    if (existingUserForEmployee.isPresent()) {
                        // If linked, only allow if it's the current user being edited
                        return isEdit && existingUserForEmployee.get().getUserID().equals(userDTO.getUserID());
                    }
                    return true; // Employee is not linked to any user
                })
                .collect(Collectors.toList());

        // Add the current employee to the list if editing and they are currently linked
        if (isEdit && userDTO.getEmployeeID() != null) {
            boolean currentEmployeePresent = availableEmployees.stream()
                    .anyMatch(emp -> emp.getEmployeeID().equals(userDTO.getEmployeeID()));
            if (!currentEmployeePresent) {
                employeeService.getEmployeeById(userDTO.getEmployeeID())
                        .ifPresent(availableEmployees::add);
            }
        }


        model.addAttribute("availableEmployees", availableEmployees);

        // Fetch all roles for the checkboxes
        List<RoleDTO> allRoles = roleService.getAllRoles();
        model.addAttribute("allRoles", allRoles);
    }

    // Display list of users
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Only ADMIN can view/manage users
    public String listUsers(Model model) {
        List<UserResponseDTO> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("pageTitle", "Users List");
        return "users/list"; // Resolves to /WEB-INF/views/users/list.jsp
    }

    // Show form for adding a new user
    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddForm(Model model) {
        UserCreateDTO userDTO = (UserCreateDTO) model.getAttribute("user");
        if (userDTO == null) {
            userDTO = new UserCreateDTO(); // Provide empty DTO if not present from redirect
            userDTO.setIsLocked(false); // Default new user to not locked
        }
        prepareUserFormModel(model, userDTO, false);
        return "users/form"; // Resolves to /WEB-INF/views/users/form.jsp
    }

    // Show form for editing an existing user
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        UserCreateDTO userDTO = (UserCreateDTO) model.getAttribute("user");
        if (userDTO == null || (userDTO.getUserID() != null && !userDTO.getUserID().equals(id))) {
            Optional<UserResponseDTO> existingUser = userService.getUserById(id);
            if (existingUser.isPresent()) {
                // Convert ResponseDTO to CreateDTO for the form
                userDTO = UserCreateDTO.builder()
                        .userID(existingUser.get().getUserID())
                        .username(existingUser.get().getUsername())
                        .employeeID(existingUser.get().getEmployeeID())
                        // Password is NOT set in DTO for security reasons during edit form load
                        .isLocked(existingUser.get().getIsLocked())
                        // Convert role names to IDs for checkboxes
                        .roleIDs(existingUser.get().getRoles().stream()
                                .map(roleName -> roleService.getAllRoles().stream() // Fetch all roles to find ID by name
                                        .filter(r -> r.getName().equals(roleName))
                                        .map(RoleDTO::getRoleID)
                                        .findFirst()
                                        .orElseThrow(() -> new IllegalStateException("Role '" + roleName + "' not found in DB!"))
                                )
                                .collect(Collectors.toSet()))
                        .build();
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "User not found!");
                return "redirect:/users";
            }
        }
        prepareUserFormModel(model, userDTO, true);
        return "users/form";
    }

    // Handle submission of add/edit form
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveUser(@Valid UserCreateDTO userDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        // Custom validation logic if needed (e.g., password blank on new, or if changed on edit)
        if (userDTO.getUserID() == null && (userDTO.getPassword() == null || userDTO.getPassword().isBlank())) {
            bindingResult.rejectValue("password", "NotBlank.user.password", "Password is required for new users.");
        }
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank() && userDTO.getPassword().length() < 8) {
            bindingResult.rejectValue("password", "Size.user.password", "Password must be at least 8 characters long.");
        }


        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred. Please check the form.");
            redirectAttributes.addFlashAttribute("user", userDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "user", bindingResult);
            return (userDTO.getUserID() != null) ? "redirect:/users/edit/" + userDTO.getUserID() : "redirect:/users/new";
        }

        try {
            if (userDTO.getUserID() == null) { // New user
                userService.createUser(userDTO);
                redirectAttributes.addFlashAttribute("successMessage", "User created successfully!");
            } else { // Existing user
                // For update, password field can be empty if not changing password.
                // The service layer handles this by only hashing if password is not null/empty.
                userService.updateUser(userDTO.getUserID(), userDTO);
                redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving user: " + e.getMessage());
            // Pass back DTO and errors to repopulate form after service exception
            redirectAttributes.addFlashAttribute("user", userDTO);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "user", bindingResult);
            return (userDTO.getUserID() != null) ? "redirect:/users/edit/" + userDTO.getUserID() : "redirect:/users/new";
        }

        return "redirect:/users";
    }

    // Handle deletion of a user
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/users";
    }
}