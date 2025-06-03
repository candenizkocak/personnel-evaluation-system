package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.GoalTypeDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.GoalType;
import com.workin.personnelevaluationsystem.repository.GoalTypeRepository;
import com.workin.personnelevaluationsystem.service.GoalTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GoalTypeServiceImpl implements GoalTypeService {

    private final GoalTypeRepository goalTypeRepository;

    @Autowired
    public GoalTypeServiceImpl(GoalTypeRepository goalTypeRepository) {
        this.goalTypeRepository = goalTypeRepository;
    }

    private GoalTypeDTO convertToDto(GoalType goalType) {
        if (goalType == null) return null;
        return GoalTypeDTO.builder()
                .goalTypeID(goalType.getGoalTypeID())
                .name(goalType.getName())
                .description(goalType.getDescription())
                .build();
    }

    private GoalType convertToEntity(GoalTypeDTO goalTypeDTO) {
        if (goalTypeDTO == null) return null;
        return GoalType.builder()
                .goalTypeID(goalTypeDTO.getGoalTypeID())
                .name(goalTypeDTO.getName())
                .description(goalTypeDTO.getDescription())
                .build();
    }

    @Override
    public GoalTypeDTO createGoalType(GoalTypeDTO typeDTO) {
        if (goalTypeRepository.findByName(typeDTO.getName()).isPresent()) {
            throw new BadRequestException("Goal type with name '" + typeDTO.getName() + "' already exists.");
        }
        GoalType goalType = convertToEntity(typeDTO);
        GoalType savedGoalType = goalTypeRepository.save(goalType);
        return convertToDto(savedGoalType);
    }

    @Override
    public Optional<GoalTypeDTO> getGoalTypeById(Integer id) {
        return goalTypeRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<GoalTypeDTO> getAllGoalTypes() {
        return goalTypeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public GoalTypeDTO updateGoalType(Integer id, GoalTypeDTO typeDetailsDTO) {
        GoalType goalTypeToUpdate = goalTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal type not found with ID: " + id));

        if (!goalTypeToUpdate.getName().equals(typeDetailsDTO.getName())) {
            goalTypeRepository.findByName(typeDetailsDTO.getName())
                    .ifPresent(gt -> {
                        if (!gt.getGoalTypeID().equals(id)) {
                            throw new BadRequestException("Goal type with name '" + typeDetailsDTO.getName() + "' already exists.");
                        }
                    });
        }

        goalTypeToUpdate.setName(typeDetailsDTO.getName());
        goalTypeToUpdate.setDescription(typeDetailsDTO.getDescription());

        GoalType updatedGoalType = goalTypeRepository.save(goalTypeToUpdate);
        return convertToDto(updatedGoalType);
    }

    @Override
    public void deleteGoalType(Integer id) {
        if (!goalTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Goal type not found with ID: " + id);
        }
        // If the type is referenced by Goals, deleting it directly will cause a DataIntegrityViolationException.
        goalTypeRepository.deleteById(id);
    }
}