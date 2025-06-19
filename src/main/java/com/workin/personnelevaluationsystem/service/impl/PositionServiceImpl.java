package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.PositionDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.Department;
import com.workin.personnelevaluationsystem.model.Position;
import com.workin.personnelevaluationsystem.repository.DepartmentRepository;
import com.workin.personnelevaluationsystem.repository.PositionRepository;
import com.workin.personnelevaluationsystem.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository, DepartmentRepository departmentRepository) {
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
    }

    private PositionDTO convertToDto(Position position) {
        if (position == null) return null;
        return PositionDTO.builder()
                .positionID(position.getPositionID())
                .title(position.getTitle())
                .description(position.getDescription())
                .departmentID(position.getDepartment() != null ? position.getDepartment().getDepartmentID() : null)
                .departmentName(position.getDepartment() != null ? position.getDepartment().getName() : null) // POPULATE THE NEW FIELD
                .isManagement(position.getIsManagement())
                .build();
    }

    private Position convertToEntity(PositionDTO positionDTO) {
        if (positionDTO == null) return null;
        Position position = Position.builder()
                .positionID(positionDTO.getPositionID())
                .title(positionDTO.getTitle())
                .description(positionDTO.getDescription())
                .isManagement(positionDTO.getIsManagement())
                .build();

        if (positionDTO.getDepartmentID() != null) {
            Department department = departmentRepository.findById(positionDTO.getDepartmentID())
                    .orElseThrow(() -> new BadRequestException("Department not found with ID: " + positionDTO.getDepartmentID() + " for position creation/update."));
            position.setDepartment(department);
        }
        return position;
    }

    @Override
    public PositionDTO createPosition(PositionDTO positionDTO) {
        Position position = convertToEntity(positionDTO);
        Position savedPosition = positionRepository.save(position);
        return convertToDto(savedPosition);
    }

    @Override
    public Optional<PositionDTO> getPositionById(Integer id) {
        return positionRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<PositionDTO> getAllPositions() {
        return positionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PositionDTO updatePosition(Integer id, PositionDTO positionDetailsDTO) {
        Position positionToUpdate = positionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Position not found with ID: " + id));

        positionToUpdate.setTitle(positionDetailsDTO.getTitle());
        positionToUpdate.setDescription(positionDetailsDTO.getDescription());
        positionToUpdate.setIsManagement(positionDetailsDTO.getIsManagement());

        if (positionDetailsDTO.getDepartmentID() != null) {
            Department newDepartment = departmentRepository.findById(positionDetailsDTO.getDepartmentID())
                    .orElseThrow(() -> new BadRequestException("Department not found with ID: " + positionDetailsDTO.getDepartmentID() + " for position update."));
            positionToUpdate.setDepartment(newDepartment);
        } else {
            positionToUpdate.setDepartment(null);
        }

        Position updatedPosition = positionRepository.save(positionToUpdate);
        return convertToDto(updatedPosition);
    }

    @Override
    public void deletePosition(Integer id) {
        if (!positionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Position not found with ID: " + id);
        }
        positionRepository.deleteById(id);
    }
}