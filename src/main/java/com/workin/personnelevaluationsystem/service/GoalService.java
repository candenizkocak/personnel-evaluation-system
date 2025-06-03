package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.GoalDTO;

import java.util.List;
import java.util.Optional;

public interface GoalService {
    GoalDTO createGoal(GoalDTO goalDTO);
    Optional<GoalDTO> getGoalById(Integer id);
    List<GoalDTO> getAllGoals();
    List<GoalDTO> getGoalsByEmployeeId(Integer employeeId);
    List<GoalDTO> getGoalsByTypeId(Integer goalTypeId);
    List<GoalDTO> getGoalsByStatusId(Integer statusId);
    GoalDTO updateGoal(Integer id, GoalDTO goalDetailsDTO);
    void deleteGoal(Integer id);
}