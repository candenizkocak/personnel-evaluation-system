package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.*;
import com.workin.personnelevaluationsystem.model.User;
import com.workin.personnelevaluationsystem.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/performance-reviews")
public class PerformanceReviewWebController {

    private final PerformanceReviewService reviewService;
    private final EmployeeService employeeService;
    private final EvaluationPeriodService periodService;
    private final EvaluationFormService formService;

    @Autowired
    public PerformanceReviewWebController(PerformanceReviewService reviewService, EmployeeService employeeService, EvaluationPeriodService periodService, EvaluationFormService formService) {
        this.reviewService = reviewService;
        this.employeeService = employeeService;
        this.periodService = periodService;
        this.formService = formService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST', 'MANAGER')")
    public String listReviews(Model model) {
        model.addAttribute("reviews", reviewService.getAllPerformanceReviews());
        model.addAttribute("pageTitle", "Performance Reviews");
        return "performance-reviews/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST', 'MANAGER')")
    public String showStartNewReviewForm(Model model, @AuthenticationPrincipal User currentUser) {
        PerformanceReviewStartDTO startDTO = new PerformanceReviewStartDTO();
        if (currentUser != null && currentUser.getEmployee() != null) {
            startDTO.setEvaluatorID(currentUser.getEmployee().getEmployeeID()); // This line is key
        } // If currentUser or employee is null, evaluatorID will remain null, requiring manual selection or error handling

        model.addAttribute("reviewStartDto", startDTO);
        model.addAttribute("employees", employeeService.getAllEmployees()); // For 'Employee to Review' dropdown
        model.addAttribute("periods", periodService.getAllEvaluationPeriods());
        model.addAttribute("forms", formService.getAllEvaluationForms());
        model.addAttribute("pageTitle", "Start New Performance Review");
        // Pass the current user's employee details for display if needed
        if (currentUser != null && currentUser.getEmployee() != null) {
            model.addAttribute("currentEvaluator", employeeService.getEmployeeById(currentUser.getEmployee().getEmployeeID()).orElse(null));
        }
        return "performance-reviews/start-form";
    }

    @PostMapping("/create-draft")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST', 'MANAGER')")
    public String createDraftReview(@Valid @ModelAttribute("reviewStartDto") PerformanceReviewStartDTO startDTO,
                                    BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            model.addAttribute("periods", periodService.getAllEvaluationPeriods());
            model.addAttribute("forms", formService.getAllEvaluationForms());
            model.addAttribute("pageTitle", "Start New Performance Review");
            return "performance-reviews/start-form";
        }

        PerformanceReviewCreateDTO createDTO = new PerformanceReviewCreateDTO();
        createDTO.setEmployeeID(startDTO.getEmployeeID());
        createDTO.setEvaluatorID(startDTO.getEvaluatorID());
        createDTO.setPeriodID(startDTO.getPeriodID());
        createDTO.setFormID(startDTO.getFormID());

        try {
            PerformanceReviewResponseDTO savedReview = reviewService.createPerformanceReview(createDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Draft review created. You can now fill out the form.");
            return "redirect:/performance-reviews/fill/" + savedReview.getReviewID();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating draft review: " + e.getMessage());
            return "redirect:/performance-reviews/new";
        }
    }

    @GetMapping("/fill/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST', 'MANAGER')")
    public String showFillReviewForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return reviewService.getPerformanceReviewById(id).map(review -> {
            ReviewSubmissionDTO submissionDTO = new ReviewSubmissionDTO();
            submissionDTO.setReviewResponses(review.getReviewResponses());
            model.addAttribute("reviewDetails", review);
            model.addAttribute("reviewSubmission", submissionDTO);
            model.addAttribute("pageTitle", "Performance Review for " + review.getEmployeeFullName());
            return "performance-reviews/fill-form";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Review not found with ID: " + id);
            return "redirect:/performance-reviews";
        });
    }

    @PostMapping("/submit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST', 'MANAGER')")
    public String submitReview(@PathVariable Integer id,
                               @ModelAttribute("reviewSubmission") ReviewSubmissionDTO submissionDTO,
                               @RequestParam("action") String action,
                               RedirectAttributes redirectAttributes) {
        try {
            if ("submit".equals(action)) {
                reviewService.submitFinalReview(id, submissionDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Review submitted successfully!");
            } else {
                reviewService.saveReviewDraft(id, submissionDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Draft saved successfully!");
                return "redirect:/performance-reviews/fill/" + id;
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error processing review: " + e.getMessage());
            return "redirect:/performance-reviews/fill/" + id;
        }

        return "redirect:/performance-reviews";
    }
    @GetMapping("/my-reviews")
    @PreAuthorize("isAuthenticated()")
    public String listMyReviews(@AuthenticationPrincipal User currentUser, Model model, RedirectAttributes redirectAttributes) {
        if (currentUser.getEmployee() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your user account is not linked to an employee profile.");
            return "redirect:/dashboard";
        }
        Integer employeeId = currentUser.getEmployee().getEmployeeID();
        List<PerformanceReviewResponseDTO> myReviews = reviewService.getReviewsByEmployee(employeeId);
        model.addAttribute("reviews", myReviews);
        model.addAttribute("pageTitle", "My Performance Reviews");
        return "performance-reviews/my-list";
    }

    @GetMapping("/average-scores")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST', 'MANAGER')") // Adjust roles as needed
    public String showAverageScoresChart(Model model) {
        List<EmployeeAverageScoreDTO> averageScores = reviewService.getEmployeeAverageScores();
        model.addAttribute("averageScoresData", averageScores);
        model.addAttribute("pageTitle", "Employee Average Scores");
        return "performance-reviews/average-scores-chart";
    }
}