package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.GoalDTO;
import com.workin.personnelevaluationsystem.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/goals")
public class GoalController {

    private final GoalService goalService;

    @Autowired
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    public ResponseEntity<GoalDTO> createGoal(@Valid @RequestBody GoalDTO goalDTO) {
        GoalDTO createdGoal = goalService.createGoal(goalDTO);
        return new ResponseEntity<>(createdGoal, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoalDTO> getGoalById(@PathVariable Integer id) {
        return goalService.getGoalById(id)
                .map(goalDTO -> new ResponseEntity<>(goalDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<GoalDTO>> getAllGoals() {
        List<GoalDTO> goals = goalService.getAllGoals();
        return new ResponseEntity<>(goals, HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<GoalDTO>> getGoalsByEmployeeId(@PathVariable Integer employeeId) {
        List<GoalDTO> goals = goalService.getGoalsByEmployeeId(employeeId);
        return new ResponseEntity<>(goals, HttpStatus.OK);
    }

    @GetMapping("/type/{goalTypeId}")
    public ResponseEntity<List<GoalDTO>> getGoalsByTypeId(@PathVariable Integer goalTypeId) {
        List<GoalDTO> goals = goalService.getGoalsByTypeId(goalTypeId);
        return new ResponseEntity<>(goals, HttpStatus.OK);
    }

    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<GoalDTO>> getGoalsByStatusId(@PathVariable Integer statusId) {
        List<GoalDTO> goals = goalService.getGoalsByStatusId(statusId);
        return new ResponseEntity<>(goals, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GoalDTO> updateGoal(@PathVariable Integer id, @Valid @RequestBody GoalDTO goalDetailsDTO) {
        GoalDTO updatedGoal = goalService.updateGoal(id, goalDetailsDTO);
        return new ResponseEntity<>(updatedGoal, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Integer id) {
        goalService.deleteGoal(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}