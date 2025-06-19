package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.CompetencyCategoryDTO;
import com.workin.personnelevaluationsystem.service.CompetencyCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/competency-categories")
@PreAuthorize("hasAnyRole('ADMIN', 'HR_SPECIALIST')")
public class CompetencyCategoryWebController {

    private final CompetencyCategoryService categoryService;

    @Autowired
    public CompetencyCategoryWebController(CompetencyCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCompetencyCategories());
        model.addAttribute("pageTitle", "Competency Categories");
        return "competency-categories/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("category", new CompetencyCategoryDTO());
        model.addAttribute("pageTitle", "Add New Category");
        return "competency-categories/form";
    }

    @PostMapping("/save")
    public String saveCategory(@Valid @ModelAttribute("category") CompetencyCategoryDTO categoryDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", categoryDTO.getCategoryID() == null ? "Add New Category" : "Edit Category");
            return "competency-categories/form";
        }
        boolean isNew = categoryDTO.getCategoryID() == null;
        try {
            if (isNew) {
                categoryService.createCompetencyCategory(categoryDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Category created successfully!");
            } else {
                categoryService.updateCompetencyCategory(categoryDTO.getCategoryID(), categoryDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Category updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/competency-categories";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return categoryService.getCompetencyCategoryById(id).map(category -> {
            model.addAttribute("category", category);
            model.addAttribute("pageTitle", "Edit Category");
            return "competency-categories/form";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Category not found.");
            return "redirect:/competency-categories";
        });
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCompetencyCategory(id);
            redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully.");
        } catch (Exception e) {
            // Log the actual error for administrators/developers
            System.err.println("Error deleting category: " + e.getMessage());
            e.printStackTrace();

            // Show user-friendly message
            redirectAttributes.addFlashAttribute("errorMessage", "Can't complete the operation as the entity is in active use.");
        }
        return "redirect:/competency-categories";
    }
}