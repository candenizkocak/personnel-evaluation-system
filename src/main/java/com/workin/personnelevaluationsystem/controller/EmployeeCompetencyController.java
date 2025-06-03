package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.EmployeeCompetencyDTO;
import com.workin.personnelevaluationsystem.service.EmployeeCompetencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat; // For LocalDate in path
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employee-competencies")
public class EmployeeCompetencyController {

    private final EmployeeCompetencyService employeeCompetencyService;

    @Autowired
    public EmployeeCompetencyController(EmployeeCompetencyService employeeCompetencyService) {
        this.employeeCompetencyService = employeeCompetencyService;
    }

    @PostMapping
    public ResponseEntity<EmployeeCompetencyDTO> createEmployeeCompetency(@Valid @RequestBody EmployeeCompetencyDTO employeeCompetencyDTO) {
        EmployeeCompetencyDTO createdEmployeeCompetency = employeeCompetencyService.createEmployeeCompetency(employeeCompetencyDTO);
        return new ResponseEntity<>(createdEmployeeCompetency, HttpStatus.CREATED);
    }

    // GET by composite key
    // Example URL: /api/v1/employee-competencies/employee/1/competency/10/assessmentDate/2023-01-15
    @GetMapping("/employee/{employeeId}/competency/{competencyId}/assessmentDate/{assessmentDate}")
    public ResponseEntity<EmployeeCompetencyDTO> getEmployeeCompetencyById(
            @PathVariable Integer employeeId,
            @PathVariable Integer competencyId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate assessmentDate) {
        return employeeCompetencyService.getEmployeeCompetencyById(employeeId, competencyId, assessmentDate)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeCompetencyDTO>> getAllEmployeeCompetencies() {
        List<EmployeeCompetencyDTO> employeeCompetencies = employeeCompetencyService.getAllEmployeeCompetencies();
        return new ResponseEntity<>(employeeCompetencies, HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeCompetencyDTO>> getEmployeeCompetenciesByEmployeeId(@PathVariable Integer employeeId) {
        List<EmployeeCompetencyDTO> employeeCompetencies = employeeCompetencyService.getEmployeeCompetenciesByEmployeeId(employeeId);
        return new ResponseEntity<>(employeeCompetencies, HttpStatus.OK);
    }

    @GetMapping("/competency/{competencyId}")
    public ResponseEntity<List<EmployeeCompetencyDTO>> getEmployeeCompetenciesByCompetencyId(@PathVariable Integer competencyId) {
        List<EmployeeCompetencyDTO> employeeCompetencies = employeeCompetencyService.getEmployeeCompetenciesByCompetencyId(competencyId);
        return new ResponseEntity<>(employeeCompetencies, HttpStatus.OK);
    }

    // PUT by composite key
    @PutMapping("/employee/{employeeId}/competency/{competencyId}/assessmentDate/{assessmentDate}")
    public ResponseEntity<EmployeeCompetencyDTO> updateEmployeeCompetency(
            @PathVariable Integer employeeId,
            @PathVariable Integer competencyId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate assessmentDate,
            @Valid @RequestBody EmployeeCompetencyDTO employeeCompetencyDetailsDTO) {
        EmployeeCompetencyDTO updatedEmployeeCompetency = employeeCompetencyService.updateEmployeeCompetency(
                employeeId, competencyId, assessmentDate, employeeCompetencyDetailsDTO);
        return new ResponseEntity<>(updatedEmployeeCompetency, HttpStatus.OK);
    }

    // DELETE by composite key
    @DeleteMapping("/employee/{employeeId}/competency/{competencyId}/assessmentDate/{assessmentDate}")
    public ResponseEntity<Void> deleteEmployeeCompetency(
            @PathVariable Integer employeeId,
            @PathVariable Integer competencyId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate assessmentDate) {
        employeeCompetencyService.deleteEmployeeCompetency(employeeId, competencyId, assessmentDate);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}