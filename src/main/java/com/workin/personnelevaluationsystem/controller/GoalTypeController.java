package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.GoalTypeDTO;
import com.workin.personnelevaluationsystem.service.GoalTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/goal-types")
public class GoalTypeController {

    private final GoalTypeService goalTypeService;

    @Autowired
    public GoalTypeController(GoalTypeService goalTypeService) {
        this.goalTypeService = goalTypeService;
    }

    @PostMapping
    public ResponseEntity<GoalTypeDTO> createGoalType(@Valid @RequestBody GoalTypeDTO typeDTO) {
        GoalTypeDTO createdType = goalTypeService.createGoalType(typeDTO);
        return new ResponseEntity<>(createdType, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoalTypeDTO> getGoalTypeById(@PathVariable Integer id) {
        return goalTypeService.getGoalTypeById(id)
                .map(typeDTO -> new ResponseEntity<>(typeDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<GoalTypeDTO>> getAllGoalTypes() {
        List<GoalTypeDTO> types = goalTypeService.getAllGoalTypes();
        return new ResponseEntity<>(types, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GoalTypeDTO> updateGoalType(@PathVariable Integer id, @Valid @RequestBody GoalTypeDTO typeDetailsDTO) {
        GoalTypeDTO updatedType = goalTypeService.updateGoalType(id, typeDetailsDTO);
        return new ResponseEntity<>(updatedType, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoalType(@PathVariable Integer id) {
        goalTypeService.deleteGoalType(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}