package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.GoalDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.Employee;
import com.workin.personnelevaluationsystem.model.Goal;
import com.workin.personnelevaluationsystem.model.GoalStatus;
import com.workin.personnelevaluationsystem.model.GoalType;
import com.workin.personnelevaluationsystem.repository.EmployeeRepository;
import com.workin.personnelevaluationsystem.repository.GoalRepository;
import com.workin.personnelevaluationsystem.repository.GoalStatusRepository;
import com.workin.personnelevaluationsystem.repository.GoalTypeRepository;
import com.workin.personnelevaluationsystem.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final EmployeeRepository employeeRepository;
    private final GoalTypeRepository goalTypeRepository;
    private final GoalStatusRepository goalStatusRepository;

    @Autowired
    public GoalServiceImpl(GoalRepository goalRepository,
                           EmployeeRepository employeeRepository,
                           GoalTypeRepository goalTypeRepository,
                           GoalStatusRepository goalStatusRepository) {
        this.goalRepository = goalRepository;
        this.employeeRepository = employeeRepository;
        this.goalTypeRepository = goalTypeRepository;
        this.goalStatusRepository = goalStatusRepository;
    }

    private GoalDTO convertToDto(Goal goal) {
        if (goal == null) return null;
        return GoalDTO.builder()
                .goalID(goal.getGoalID())
                .employeeID(goal.getEmployee() != null ? goal.getEmployee().getEmployeeID() : null)
                .title(goal.getTitle())
                .description(goal.getDescription())
                .goalTypeID(goal.getGoalType() != null ? goal.getGoalType().getGoalTypeID() : null)
                .statusID(goal.getGoalStatus() != null ? goal.getGoalStatus().getStatusID() : null)
                .startDate(goal.getStartDate())
                .targetDate(goal.getTargetDate())
                .completionDate(goal.getCompletionDate())
                .progress(goal.getProgress())
                .build();
    }

    private Goal convertToEntity(GoalDTO goalDTO) {
        if (goalDTO == null) return null;

        Employee employee = employeeRepository.findById(goalDTO.getEmployeeID())
                .orElseThrow(() -> new BadRequestException("Employee not found with ID: " + goalDTO.getEmployeeID()));
        GoalType goalType = goalTypeRepository.findById(goalDTO.getGoalTypeID())
                .orElseThrow(() -> new BadRequestException("Goal Type not found with ID: " + goalDTO.getGoalTypeID()));
        GoalStatus goalStatus = goalStatusRepository.findById(goalDTO.getStatusID())
                .orElseThrow(() -> new BadRequestException("Goal Status not found with ID: " + goalDTO.getStatusID()));

        // Business logic validation for dates
        if (goalDTO.getStartDate() != null && goalDTO.getTargetDate() != null &&
                goalDTO.getStartDate().isAfter(goalDTO.getTargetDate())) {
            throw new BadRequestException("Goal start date cannot be after target date.");
        }
        if (goalDTO.getCompletionDate() != null) {
            if (goalDTO.getStartDate() != null && goalDTO.getCompletionDate().isBefore(goalDTO.getStartDate())) {
                throw new BadRequestException("Goal completion date cannot be before start date.");
            }
            if (goalDTO.getTargetDate() != null && goalDTO.getCompletionDate().isAfter(goalDTO.getTargetDate())) {
                // This rule might vary. Sometimes completion can be after target.
                // For now, let's allow it, but a warning could be issued.
                // throw new BadRequestException("Goal completion date cannot be after target date.");
            }
        }


        return Goal.builder()
                .goalID(goalDTO.getGoalID())
                .employee(employee)
                .title(goalDTO.getTitle())
                .description(goalDTO.getDescription())
                .goalType(goalType)
                .goalStatus(goalStatus)
                .startDate(goalDTO.getStartDate())
                .targetDate(goalDTO.getTargetDate())
                .completionDate(goalDTO.getCompletionDate())
                .progress(goalDTO.getProgress() != null ? goalDTO.getProgress() : BigDecimal.ZERO) // Default to 0.00
                .build();
    }

    @Override
    @Transactional
    public GoalDTO createGoal(GoalDTO goalDTO) {
        Goal goal = convertToEntity(goalDTO);
        Goal savedGoal = goalRepository.save(goal);
        return convertToDto(savedGoal);
    }

    @Override
    public Optional<GoalDTO> getGoalById(Integer id) {
        return goalRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<GoalDTO> getAllGoals() {
        return goalRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GoalDTO> getGoalsByEmployeeId(Integer employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee not found with ID: " + employeeId);
        }
        return goalRepository.findByEmployee_EmployeeID(employeeId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GoalDTO> getGoalsByTypeId(Integer goalTypeId) {
        if (!goalTypeRepository.existsById(goalTypeId)) {
            throw new ResourceNotFoundException("Goal Type not found with ID: " + goalTypeId);
        }
        return goalRepository.findByGoalType_GoalTypeID(goalTypeId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GoalDTO> getGoalsByStatusId(Integer statusId) {
        if (!goalStatusRepository.existsById(statusId)) {
            throw new ResourceNotFoundException("Goal Status not found with ID: " + statusId);
        }
        return goalRepository.findByGoalStatus_StatusID(statusId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GoalDTO updateGoal(Integer id, GoalDTO goalDetailsDTO) {
        Goal goalToUpdate = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found with ID: " + id));

        // Update relationships and simple fields
        if (goalDetailsDTO.getEmployeeID() != null) {
            Employee newEmployee = employeeRepository.findById(goalDetailsDTO.getEmployeeID())
                    .orElseThrow(() -> new BadRequestException("Employee not found with ID: " + goalDetailsDTO.getEmployeeID()));
            goalToUpdate.setEmployee(newEmployee);
        }

        if (goalDetailsDTO.getGoalTypeID() != null) {
            GoalType newGoalType = goalTypeRepository.findById(goalDetailsDTO.getGoalTypeID())
                    .orElseThrow(() -> new BadRequestException("Goal Type not found with ID: " + goalDetailsDTO.getGoalTypeID()));
            goalToUpdate.setGoalType(newGoalType);
        }

        if (goalDetailsDTO.getStatusID() != null) {
            GoalStatus newGoalStatus = goalStatusRepository.findById(goalDetailsDTO.getStatusID())
                    .orElseThrow(() -> new BadRequestException("Goal Status not found with ID: " + goalDetailsDTO.getStatusID()));
            goalToUpdate.setGoalStatus(newGoalStatus);
        }

        // Update scalar fields, handling nulls to retain existing values if DTO field is null
        goalToUpdate.setTitle(goalDetailsDTO.getTitle() != null ? goalDetailsDTO.getTitle() : goalToUpdate.getTitle());
        goalToUpdate.setDescription(goalDetailsDTO.getDescription() != null ? goalDetailsDTO.getDescription() : goalToUpdate.getDescription());
        goalToUpdate.setStartDate(goalDetailsDTO.getStartDate() != null ? goalDetailsDTO.getStartDate() : goalToUpdate.getStartDate());
        goalToUpdate.setTargetDate(goalDetailsDTO.getTargetDate() != null ? goalDetailsDTO.getTargetDate() : goalToUpdate.getTargetDate());
        goalToUpdate.setCompletionDate(goalDetailsDTO.getCompletionDate() != null ? goalDetailsDTO.getCompletionDate() : goalToUpdate.getCompletionDate());
        goalToUpdate.setProgress(goalDetailsDTO.getProgress() != null ? goalDetailsDTO.getProgress() : goalToUpdate.getProgress());

        // Re-run date validation after potential updates
        if (goalToUpdate.getStartDate() != null && goalToUpdate.getTargetDate() != null &&
                goalToUpdate.getStartDate().isAfter(goalToUpdate.getTargetDate())) {
            throw new BadRequestException("Goal start date cannot be after target date.");
        }
        if (goalToUpdate.getCompletionDate() != null) {
            if (goalToUpdate.getStartDate() != null && goalToUpdate.getCompletionDate().isBefore(goalToUpdate.getStartDate())) {
                throw new BadRequestException("Goal completion date cannot be before start date.");
            }
        }


        Goal updatedGoal = goalRepository.save(goalToUpdate);
        return convertToDto(updatedGoal);
    }

    @Override
    public void deleteGoal(Integer id) {
        if (!goalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Goal not found with ID: " + id);
        }
        goalRepository.deleteById(id);
    }
}