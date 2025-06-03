package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.GoalStatusDTO;
import com.workin.personnelevaluationsystem.service.GoalStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/goal-statuses")
public class GoalStatusController {

    private final GoalStatusService goalStatusService;

    @Autowired
    public GoalStatusController(GoalStatusService goalStatusService) {
        this.goalStatusService = goalStatusService;
    }

    @PostMapping
    public ResponseEntity<GoalStatusDTO> createGoalStatus(@Valid @RequestBody GoalStatusDTO statusDTO) {
        GoalStatusDTO createdStatus = goalStatusService.createGoalStatus(statusDTO);
        return new ResponseEntity<>(createdStatus, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoalStatusDTO> getGoalStatusById(@PathVariable Integer id) {
        return goalStatusService.getGoalStatusById(id)
                .map(statusDTO -> new ResponseEntity<>(statusDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<GoalStatusDTO>> getAllGoalStatuses() {
        List<GoalStatusDTO> statuses = goalStatusService.getAllGoalStatuses();
        return new ResponseEntity<>(statuses, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GoalStatusDTO> updateGoalStatus(@PathVariable Integer id, @Valid @RequestBody GoalStatusDTO statusDetailsDTO) {
        GoalStatusDTO updatedStatus = goalStatusService.updateGoalStatus(id, statusDetailsDTO);
        return new ResponseEntity<>(updatedStatus, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoalStatus(@PathVariable Integer id) {
        goalStatusService.deleteGoalStatus(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}