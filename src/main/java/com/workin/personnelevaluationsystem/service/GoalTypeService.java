package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.GoalTypeDTO;

import java.util.List;
import java.util.Optional;

public interface GoalTypeService {
    GoalTypeDTO createGoalType(GoalTypeDTO typeDTO);
    Optional<GoalTypeDTO> getGoalTypeById(Integer id);
    List<GoalTypeDTO> getAllGoalTypes();
    GoalTypeDTO updateGoalType(Integer id, GoalTypeDTO typeDetailsDTO);
    void deleteGoalType(Integer id);
}