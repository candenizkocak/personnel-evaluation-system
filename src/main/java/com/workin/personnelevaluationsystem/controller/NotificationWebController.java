// src/main/java/com/workin/personnelevaluationsystem/controller/NotificationWebController.java
package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.NotificationResponseDTO;
import com.workin.personnelevaluationsystem.model.User;
import com.workin.personnelevaluationsystem.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/notifications")
@PreAuthorize("isAuthenticated()") // All authenticated users can view their notifications
public class NotificationWebController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationWebController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public String listMyNotifications(@AuthenticationPrincipal User currentUser, Model model, RedirectAttributes redirectAttributes) {
        if (currentUser.getEmployee() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your user account is not linked to an employee profile to receive notifications.");
            return "redirect:/dashboard";
        }

        Integer employeeId = currentUser.getEmployee().getEmployeeID();
        List<NotificationResponseDTO> notifications = notificationService.getNotificationsByEmployeeId(employeeId);

        model.addAttribute("notifications", notifications);
        model.addAttribute("pageTitle", "My Notifications");
        return "notifications/list"; // This will resolve to /WEB-INF/views/notifications/list.jsp
    }

    @GetMapping("/mark-read/{id}")
    public String markAsRead(@PathVariable Integer id, @AuthenticationPrincipal User currentUser, RedirectAttributes redirectAttributes) {
        // Optional: Add a check to ensure the user owns this notification before marking it as read
        try {
            notificationService.markNotificationAsRead(id);
            redirectAttributes.addFlashAttribute("successMessage", "Notification marked as read.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating notification: " + e.getMessage());
        }
        return "redirect:/notifications";
    }

    @GetMapping("/delete/{id}")
    public String deleteNotification(@PathVariable Integer id, @AuthenticationPrincipal User currentUser, RedirectAttributes redirectAttributes) {
        // Optional: Add a check to ensure the user owns this notification before deleting
        try {
            notificationService.deleteNotification(id);
            redirectAttributes.addFlashAttribute("successMessage", "Notification deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting notification: " + e.getMessage());
        }
        return "redirect:/notifications";
    }
}