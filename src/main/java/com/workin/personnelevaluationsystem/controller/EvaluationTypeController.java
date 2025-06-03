package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.EvaluationTypeDTO;
import com.workin.personnelevaluationsystem.service.EvaluationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/evaluation-types")
public class EvaluationTypeController {

    private final EvaluationTypeService typeService;

    @Autowired
    public EvaluationTypeController(EvaluationTypeService typeService) {
        this.typeService = typeService;
    }

    @PostMapping
    public ResponseEntity<EvaluationTypeDTO> createEvaluationType(@Valid @RequestBody EvaluationTypeDTO typeDTO) {
        EvaluationTypeDTO createdType = typeService.createEvaluationType(typeDTO);
        return new ResponseEntity<>(createdType, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationTypeDTO> getEvaluationTypeById(@PathVariable Integer id) {
        return typeService.getEvaluationTypeById(id)
                .map(typeDTO -> new ResponseEntity<>(typeDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<EvaluationTypeDTO>> getAllEvaluationTypes() {
        List<EvaluationTypeDTO> types = typeService.getAllEvaluationTypes();
        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluationTypeDTO> updateEvaluationType(@PathVariable Integer id, @Valid @RequestBody EvaluationTypeDTO typeDetailsDTO) {
        EvaluationTypeDTO updatedType = typeService.updateEvaluationType(id, typeDetailsDTO);
        return new ResponseEntity<>(updatedType, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluationType(@PathVariable Integer id) {
        typeService.deleteEvaluationType(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}