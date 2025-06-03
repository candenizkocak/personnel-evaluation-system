package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.GoalStatusDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.GoalStatus;
import com.workin.personnelevaluationsystem.repository.GoalStatusRepository;
import com.workin.personnelevaluationsystem.service.GoalStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GoalStatusServiceImpl implements GoalStatusService {

    private final GoalStatusRepository goalStatusRepository;

    @Autowired
    public GoalStatusServiceImpl(GoalStatusRepository goalStatusRepository) {
        this.goalStatusRepository = goalStatusRepository;
    }

    private GoalStatusDTO convertToDto(GoalStatus goalStatus) {
        if (goalStatus == null) return null;
        return GoalStatusDTO.builder()
                .statusID(goalStatus.getStatusID())
                .name(goalStatus.getName())
                .description(goalStatus.getDescription())
                .build();
    }

    private GoalStatus convertToEntity(GoalStatusDTO goalStatusDTO) {
        if (goalStatusDTO == null) return null;
        return GoalStatus.builder()
                .statusID(goalStatusDTO.getStatusID())
                .name(goalStatusDTO.getName())
                .description(goalStatusDTO.getDescription())
                .build();
    }

    @Override
    public GoalStatusDTO createGoalStatus(GoalStatusDTO statusDTO) {
        if (goalStatusRepository.findByName(statusDTO.getName()).isPresent()) {
            throw new BadRequestException("Goal status with name '" + statusDTO.getName() + "' already exists.");
        }
        GoalStatus goalStatus = convertToEntity(statusDTO);
        GoalStatus savedGoalStatus = goalStatusRepository.save(goalStatus);
        return convertToDto(savedGoalStatus);
    }

    @Override
    public Optional<GoalStatusDTO> getGoalStatusById(Integer id) {
        return goalStatusRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<GoalStatusDTO> getAllGoalStatuses() {
        return goalStatusRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public GoalStatusDTO updateGoalStatus(Integer id, GoalStatusDTO statusDetailsDTO) {
        GoalStatus goalStatusToUpdate = goalStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal status not found with ID: " + id));

        if (!goalStatusToUpdate.getName().equals(statusDetailsDTO.getName())) {
            goalStatusRepository.findByName(statusDetailsDTO.getName())
                    .ifPresent(gs -> {
                        if (!gs.getStatusID().equals(id)) {
                            throw new BadRequestException("Goal status with name '" + statusDetailsDTO.getName() + "' already exists.");
                        }
                    });
        }

        goalStatusToUpdate.setName(statusDetailsDTO.getName());
        goalStatusToUpdate.setDescription(statusDetailsDTO.getDescription());

        GoalStatus updatedGoalStatus = goalStatusRepository.save(goalStatusToUpdate);
        return convertToDto(updatedGoalStatus);
    }

    @Override
    public void deleteGoalStatus(Integer id) {
        if (!goalStatusRepository.existsById(id)) {
            throw new ResourceNotFoundException("Goal status not found with ID: " + id);
        }
        // If the status is referenced by Goals, deleting it directly will cause a DataIntegrityViolationException.
        goalStatusRepository.deleteById(id);
    }
}