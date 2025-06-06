package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.FeedbackTypeDTO;
import com.workin.personnelevaluationsystem.service.FeedbackTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/feedback-types")
@PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
public class FeedbackTypeWebController {

    private final FeedbackTypeService feedbackTypeService;

    @Autowired
    public FeedbackTypeWebController(FeedbackTypeService feedbackTypeService) {
        this.feedbackTypeService = feedbackTypeService;
    }

    @GetMapping
    public String listFeedbackTypes(Model model) {
        model.addAttribute("feedbackTypes", feedbackTypeService.getAllFeedbackTypes());
        model.addAttribute("pageTitle", "Feedback Types");
        return "feedback-types/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("feedbackType", new FeedbackTypeDTO());
        model.addAttribute("pageTitle", "Add New Feedback Type");
        return "feedback-types/form";
    }

    @PostMapping("/save")
    public String saveFeedbackType(@Valid @ModelAttribute("feedbackType") FeedbackTypeDTO feedbackTypeDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", feedbackTypeDTO.getFeedbackTypeID() == null ? "Add New Feedback Type" : "Edit Feedback Type");
            return "feedback-types/form";
        }
        boolean isNew = feedbackTypeDTO.getFeedbackTypeID() == null;
        try {
            if (isNew) {
                feedbackTypeService.createFeedbackType(feedbackTypeDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Feedback Type created successfully!");
            } else {
                feedbackTypeService.updateFeedbackType(feedbackTypeDTO.getFeedbackTypeID(), feedbackTypeDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Feedback Type updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/feedback-types";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return feedbackTypeService.getFeedbackTypeById(id).map(feedbackType -> {
            model.addAttribute("feedbackType", feedbackType);
            model.addAttribute("pageTitle", "Edit Feedback Type");
            return "feedback-types/form";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Feedback Type not found.");
            return "redirect:/feedback-types";
        });
    }

    @GetMapping("/delete/{id}")
    public String deleteFeedbackType(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            feedbackTypeService.deleteFeedbackType(id);
            redirectAttributes.addFlashAttribute("successMessage", "Feedback Type deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting type: " + e.getMessage());
        }
        return "redirect:/feedback-types";
    }
}