package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.GoalStatusDTO;

import java.util.List;
import java.util.Optional;

public interface GoalStatusService {
    GoalStatusDTO createGoalStatus(GoalStatusDTO statusDTO);
    Optional<GoalStatusDTO> getGoalStatusById(Integer id);
    List<GoalStatusDTO> getAllGoalStatuses();
    GoalStatusDTO updateGoalStatus(Integer id, GoalStatusDTO statusDetailsDTO);
    void deleteGoalStatus(Integer id);
}