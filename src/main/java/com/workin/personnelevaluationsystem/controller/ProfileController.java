// src/main/java/com/workin/personnelevaluationsystem/controller/ProfileController.java
package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.PasswordChangeDTO;
import com.workin.personnelevaluationsystem.model.User;
import com.workin.personnelevaluationsystem.service.EmployeeService;
import com.workin.personnelevaluationsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
@PreAuthorize("isAuthenticated()")
public class ProfileController {

    private final EmployeeService employeeService;
    private final UserService userService;

    @Autowired
    public ProfileController(EmployeeService employeeService, UserService userService) {
        this.employeeService = employeeService;
        this.userService = userService;
    }

    @GetMapping("/my-profile")
    public String viewMyProfile(@AuthenticationPrincipal User currentUser, Model model, RedirectAttributes redirectAttributes) {
        if (currentUser.getEmployee() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your user account is not linked to an employee profile.");
            return "redirect:/dashboard";
        }
        Integer employeeId = currentUser.getEmployee().getEmployeeID();
        return employeeService.getEmployeeById(employeeId)
                .map(employeeDTO -> {
                    model.addAttribute("employee", employeeDTO);
                    model.addAttribute("pageTitle", "My Profile");
                    return "profile/view";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Employee profile not found.");
                    return "redirect:/dashboard";
                });
    }

    @GetMapping("/settings")
    public String showSettingsPage(Model model) {
        model.addAttribute("passwordChangeDTO", new PasswordChangeDTO());
        model.addAttribute("pageTitle", "Settings");
        return "profile/settings";
    }

    @PostMapping("/settings/change-password")
    public String changePassword(@AuthenticationPrincipal User currentUser,
                                 @Valid @ModelAttribute("passwordChangeDTO") PasswordChangeDTO passwordChangeDTO,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes, Model model) {

        if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "Passwords.mismatch", "New passwords do not match.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Settings");
            return "profile/settings";
        }

        try {
            userService.changePassword(currentUser.getUserID(), passwordChangeDTO.getOldPassword(), passwordChangeDTO.getNewPassword());
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error changing password: " + e.getMessage());
            return "redirect:/profile/settings";
        }

        return "redirect:/profile/settings";
    }
}