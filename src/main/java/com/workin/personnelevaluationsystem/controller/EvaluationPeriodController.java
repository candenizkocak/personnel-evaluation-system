package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.EvaluationPeriodDTO;
import com.workin.personnelevaluationsystem.service.EvaluationPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/evaluation-periods")
public class EvaluationPeriodController {

    private final EvaluationPeriodService periodService;

    @Autowired
    public EvaluationPeriodController(EvaluationPeriodService periodService) {
        this.periodService = periodService;
    }

    @PostMapping
    public ResponseEntity<EvaluationPeriodDTO> createEvaluationPeriod(@Valid @RequestBody EvaluationPeriodDTO periodDTO) {
        EvaluationPeriodDTO createdPeriod = periodService.createEvaluationPeriod(periodDTO);
        return new ResponseEntity<>(createdPeriod, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationPeriodDTO> getEvaluationPeriodById(@PathVariable Integer id) {
        return periodService.getEvaluationPeriodById(id)
                .map(periodDTO -> new ResponseEntity<>(periodDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<EvaluationPeriodDTO>> getAllEvaluationPeriods() {
        List<EvaluationPeriodDTO> periods = periodService.getAllEvaluationPeriods();
        return new ResponseEntity<>(periods, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluationPeriodDTO> updateEvaluationPeriod(@PathVariable Integer id, @Valid @RequestBody EvaluationPeriodDTO periodDetailsDTO) {
        EvaluationPeriodDTO updatedPeriod = periodService.updateEvaluationPeriod(id, periodDetailsDTO);
        return new ResponseEntity<>(updatedPeriod, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluationPeriod(@PathVariable Integer id) {
        periodService.deleteEvaluationPeriod(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}