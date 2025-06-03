package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.CompetencyDTO;
import com.workin.personnelevaluationsystem.service.CompetencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/competencies")
public class CompetencyController {

    private final CompetencyService competencyService;

    @Autowired
    public CompetencyController(CompetencyService competencyService) {
        this.competencyService = competencyService;
    }

    @PostMapping
    public ResponseEntity<CompetencyDTO> createCompetency(@Valid @RequestBody CompetencyDTO competencyDTO) {
        CompetencyDTO createdCompetency = competencyService.createCompetency(competencyDTO);
        return new ResponseEntity<>(createdCompetency, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetencyDTO> getCompetencyById(@PathVariable Integer id) {
        return competencyService.getCompetencyById(id)
                .map(competencyDTO -> new ResponseEntity<>(competencyDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<CompetencyDTO>> getAllCompetencies() {
        List<CompetencyDTO> competencies = competencyService.getAllCompetencies();
        return new ResponseEntity<>(competencies, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CompetencyDTO>> getCompetenciesByCategoryId(@PathVariable Integer categoryId) {
        List<CompetencyDTO> competencies = competencyService.getCompetenciesByCategoryId(categoryId);
        return new ResponseEntity<>(competencies, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetencyDTO> updateCompetency(@PathVariable Integer id, @Valid @RequestBody CompetencyDTO competencyDetailsDTO) {
        CompetencyDTO updatedCompetency = competencyService.updateCompetency(id, competencyDetailsDTO);
        return new ResponseEntity<>(updatedCompetency, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetency(@PathVariable Integer id) {
        competencyService.deleteCompetency(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}