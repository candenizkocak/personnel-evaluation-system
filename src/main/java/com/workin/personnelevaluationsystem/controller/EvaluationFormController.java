package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.EvaluationFormDTO;
import com.workin.personnelevaluationsystem.service.EvaluationFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/evaluation-forms")
public class EvaluationFormController {

    private final EvaluationFormService formService;

    @Autowired
    public EvaluationFormController(EvaluationFormService formService) {
        this.formService = formService;
    }

    @PostMapping
    public ResponseEntity<EvaluationFormDTO> createEvaluationForm(@Valid @RequestBody EvaluationFormDTO formDTO) {
        EvaluationFormDTO createdForm = formService.createEvaluationForm(formDTO);
        return new ResponseEntity<>(createdForm, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationFormDTO> getEvaluationFormById(@PathVariable Integer id) {
        return formService.getEvaluationFormById(id)
                .map(formDTO -> new ResponseEntity<>(formDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<EvaluationFormDTO>> getAllEvaluationForms() {
        List<EvaluationFormDTO> forms = formService.getAllEvaluationForms();
        return new ResponseEntity<>(forms, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluationFormDTO> updateEvaluationForm(@PathVariable Integer id, @Valid @RequestBody EvaluationFormDTO formDetailsDTO) {
        EvaluationFormDTO updatedForm = formService.updateEvaluationForm(id, formDetailsDTO);
        return new ResponseEntity<>(updatedForm, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluationForm(@PathVariable Integer id) {
        formService.deleteEvaluationForm(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}