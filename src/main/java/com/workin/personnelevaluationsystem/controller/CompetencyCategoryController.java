package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.CompetencyCategoryDTO;
import com.workin.personnelevaluationsystem.service.CompetencyCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/competency-categories")
public class CompetencyCategoryController {

    private final CompetencyCategoryService categoryService;

    @Autowired
    public CompetencyCategoryController(CompetencyCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CompetencyCategoryDTO> createCompetencyCategory(@Valid @RequestBody CompetencyCategoryDTO categoryDTO) {
        CompetencyCategoryDTO createdCategory = categoryService.createCompetencyCategory(categoryDTO);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetencyCategoryDTO> getCompetencyCategoryById(@PathVariable Integer id) {
        return categoryService.getCompetencyCategoryById(id)
                .map(categoryDTO -> new ResponseEntity<>(categoryDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<CompetencyCategoryDTO>> getAllCompetencyCategories() {
        List<CompetencyCategoryDTO> categories = categoryService.getAllCompetencyCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetencyCategoryDTO> updateCompetencyCategory(@PathVariable Integer id, @Valid @RequestBody CompetencyCategoryDTO categoryDetailsDTO) {
        CompetencyCategoryDTO updatedCategory = categoryService.updateCompetencyCategory(id, categoryDetailsDTO);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetencyCategory(@PathVariable Integer id) {
        categoryService.deleteCompetencyCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}