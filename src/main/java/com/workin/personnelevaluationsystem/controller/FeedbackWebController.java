package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.FeedbackCreateDTO;
import com.workin.personnelevaluationsystem.model.User;
import com.workin.personnelevaluationsystem.service.EmployeeService;
import com.workin.personnelevaluationsystem.service.FeedbackService;
import com.workin.personnelevaluationsystem.service.FeedbackTypeService;
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

import java.util.stream.Collectors;

@Controller
@RequestMapping("/feedback")
public class FeedbackWebController {

    private final FeedbackService feedbackService;
    private final EmployeeService employeeService;
    private final FeedbackTypeService feedbackTypeService;

    @Autowired
    public FeedbackWebController(FeedbackService feedbackService, EmployeeService employeeService, FeedbackTypeService feedbackTypeService) {
        this.feedbackService = feedbackService;
        this.employeeService = employeeService;
        this.feedbackTypeService = feedbackTypeService;
    }

    @GetMapping("/give")
    @PreAuthorize("isAuthenticated()")
    public String showGiveFeedbackForm(Model model, @AuthenticationPrincipal User currentUser) {
        FeedbackCreateDTO feedbackDTO = new FeedbackCreateDTO();
        feedbackDTO.setSenderID(currentUser.getEmployee().getEmployeeID());
        feedbackDTO.setIsAnonymous(false);

        model.addAttribute("feedback", feedbackDTO);
        // Employees can't give feedback to themselves
        model.addAttribute("receivers", employeeService.getAllEmployees().stream()
                .filter(e -> !e.getEmployeeID().equals(currentUser.getEmployee().getEmployeeID()))
                .collect(Collectors.toList()));
        model.addAttribute("feedbackTypes", feedbackTypeService.getAllFeedbackTypes());
        model.addAttribute("pageTitle", "Give Feedback");
        return "feedback/form";
    }

    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()")
    public String saveFeedback(@Valid @ModelAttribute("feedback") FeedbackCreateDTO feedbackDTO,
                               BindingResult bindingResult,
                               @AuthenticationPrincipal User currentUser,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        // Ensure the sender is the logged-in user
        feedbackDTO.setSenderID(currentUser.getEmployee().getEmployeeID());

        if (bindingResult.hasErrors()) {
            model.addAttribute("receivers", employeeService.getAllEmployees().stream()
                    .filter(e -> !e.getEmployeeID().equals(currentUser.getEmployee().getEmployeeID()))
                    .collect(Collectors.toList()));
            model.addAttribute("feedbackTypes", feedbackTypeService.getAllFeedbackTypes());
            model.addAttribute("pageTitle", "Give Feedback");
            return "feedback/form";
        }

        try {
            feedbackService.createFeedback(feedbackDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Thank you! Your feedback has been submitted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error submitting feedback: " + e.getMessage());
        }

        return "redirect:/dashboard"; // Redirect to dashboard or a "thank you" page
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
    public String listAllFeedback(Model model) {
        model.addAttribute("feedbackList", feedbackService.getAllFeedback());
        model.addAttribute("pageTitle", "All Submitted Feedback");
        return "feedback/list";
    }
}