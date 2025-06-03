package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.CompetencyLevelDTO;
import com.workin.personnelevaluationsystem.service.CompetencyLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/competency-levels")
public class CompetencyLevelController {

    private final CompetencyLevelService levelService;

    @Autowired
    public CompetencyLevelController(CompetencyLevelService levelService) {
        this.levelService = levelService;
    }

    // Endpoint for creating a level for a specific competency
    @PostMapping("/competency/{competencyId}")
    public ResponseEntity<CompetencyLevelDTO> createCompetencyLevelForCompetency(
            @PathVariable Integer competencyId,
            @Valid @RequestBody CompetencyLevelDTO levelDTO) {
        CompetencyLevelDTO createdLevel = levelService.createCompetencyLevel(competencyId, levelDTO);
        return new ResponseEntity<>(createdLevel, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetencyLevelDTO> getCompetencyLevelById(@PathVariable Integer id) {
        return levelService.getCompetencyLevelById(id)
                .map(levelDTO -> new ResponseEntity<>(levelDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/competency/{competencyId}")
    public ResponseEntity<List<CompetencyLevelDTO>> getCompetencyLevelsByCompetencyId(@PathVariable Integer competencyId) {
        List<CompetencyLevelDTO> levels = levelService.getCompetencyLevelsByCompetencyId(competencyId);
        return new ResponseEntity<>(levels, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetencyLevelDTO> updateCompetencyLevel(@PathVariable Integer id, @Valid @RequestBody CompetencyLevelDTO levelDetailsDTO) {
        CompetencyLevelDTO updatedLevel = levelService.updateCompetencyLevel(id, levelDetailsDTO);
        return new ResponseEntity<>(updatedLevel, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetencyLevel(@PathVariable Integer id) {
        levelService.deleteCompetencyLevel(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}