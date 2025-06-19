package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.PerformanceReviewResponseDTO;
import com.workin.personnelevaluationsystem.model.User;
import com.workin.personnelevaluationsystem.service.PerformanceReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WebController {

    @Autowired
    private PerformanceReviewService performanceReviewService;

    @GetMapping({"/", "/home"})
    public String homePage() {
        return "home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboardPage(Model model, @AuthenticationPrincipal User currentUser) {
        // For debugging in console
        System.out.println("--- Dashboard Access ---");
        System.out.println("User: " + (currentUser != null ? currentUser.getUsername() : "ANONYMOUS"));

        if (currentUser != null && currentUser.getEmployee() != null) {
            Integer employeeId = currentUser.getEmployee().getEmployeeID();
            System.out.println("Employee ID for chart: " + employeeId);

            List<PerformanceReviewResponseDTO> employeeReviews = performanceReviewService.getReviewsByEmployee(employeeId);
            System.out.println("Total reviews fetched for employee: " + employeeReviews.size());
            // employeeReviews.forEach(r -> System.out.println("Fetched Review Details: ID=" + r.getReviewID() + ", Status=" + r.getStatus() + ", Score=" + r.getFinalScore() + ", Date=" + r.getSubmissionDate() + ", Period=" + r.getPeriodName()));


            List<PerformanceReviewResponseDTO> chartData = employeeReviews.stream()
                    .filter(review -> "Submitted".equalsIgnoreCase(review.getStatus()) &&
                            review.getFinalScore() != null &&
                            review.getSubmissionDate() != null)
                    .sorted(Comparator.comparing(PerformanceReviewResponseDTO::getSubmissionDate))
                    .collect(Collectors.toList());

            System.out.println("Filtered reviews for chart (Submitted, with Score & Date): " + chartData.size());
            // chartData.forEach(r -> System.out.println("Chart Data Point: Score=" + r.getFinalScore() + ", Date=" + r.getSubmissionDate() + ", Period=" + r.getPeriodName()));


            if (!chartData.isEmpty()) {
                List<String> reviewLabels = chartData.stream()
                        .map(review -> {
                            String datePart = review.getSubmissionDate().format(DateTimeFormatter.ofPattern("MMM yyyy"));
                            String periodPart = review.getPeriodName() != null ? review.getPeriodName() : "N/A";
                            return datePart + " (" + periodPart + ")";
                        })
                        .collect(Collectors.toList());

                List<Double> reviewScores = chartData.stream()
                        .map(review -> review.getFinalScore().doubleValue())
                        .collect(Collectors.toList());

                System.out.println("Chart Labels: " + reviewLabels);
                System.out.println("Chart Scores: " + reviewScores);

                model.addAttribute("reviewLabels", reviewLabels);
                model.addAttribute("reviewScores", reviewScores);
                model.addAttribute("hasChartData", true);
            } else {
                System.out.println("No valid chart data after filtering.");
                model.addAttribute("hasChartData", false);
            }
        } else {
            System.out.println("User is null or not linked to an employee. No chart data.");
            model.addAttribute("hasChartData", false);
        }
        System.out.println("--- End Dashboard Access ---");
        return "dashboard";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
}