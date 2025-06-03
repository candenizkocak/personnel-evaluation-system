package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.EvaluationPeriodDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.EvaluationPeriod;
import com.workin.personnelevaluationsystem.repository.EvaluationPeriodRepository;
import com.workin.personnelevaluationsystem.service.EvaluationPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EvaluationPeriodServiceImpl implements EvaluationPeriodService {

    private final EvaluationPeriodRepository periodRepository;

    @Autowired
    public EvaluationPeriodServiceImpl(EvaluationPeriodRepository periodRepository) {
        this.periodRepository = periodRepository;
    }

    private EvaluationPeriodDTO convertToDto(EvaluationPeriod period) {
        if (period == null) return null;
        return EvaluationPeriodDTO.builder()
                .periodID(period.getPeriodID())
                .name(period.getName())
                .startDate(period.getStartDate())
                .endDate(period.getEndDate())
                .isActive(period.getIsActive())
                .build();
    }

    private EvaluationPeriod convertToEntity(EvaluationPeriodDTO periodDTO) {
        if (periodDTO == null) return null;

        // Basic date validation
        if (periodDTO.getStartDate() != null && periodDTO.getEndDate() != null &&
                periodDTO.getStartDate().isAfter(periodDTO.getEndDate())) {
            throw new BadRequestException("Start date cannot be after end date.");
        }

        return EvaluationPeriod.builder()
                .periodID(periodDTO.getPeriodID())
                .name(periodDTO.getName())
                .startDate(periodDTO.getStartDate())
                .endDate(periodDTO.getEndDate())
                .isActive(periodDTO.getIsActive())
                .build();
    }

    @Override
    public EvaluationPeriodDTO createEvaluationPeriod(EvaluationPeriodDTO periodDTO) {
        if (periodRepository.findByName(periodDTO.getName()).isPresent()) {
            throw new BadRequestException("Evaluation period with name '" + periodDTO.getName() + "' already exists.");
        }
        EvaluationPeriod period = convertToEntity(periodDTO);
        EvaluationPeriod savedPeriod = periodRepository.save(period);
        return convertToDto(savedPeriod);
    }

    @Override
    public Optional<EvaluationPeriodDTO> getEvaluationPeriodById(Integer id) {
        return periodRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<EvaluationPeriodDTO> getAllEvaluationPeriods() {
        return periodRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EvaluationPeriodDTO updateEvaluationPeriod(Integer id, EvaluationPeriodDTO periodDetailsDTO) {
        EvaluationPeriod periodToUpdate = periodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation period not found with ID: " + id));

        // Basic date validation for update
        if (periodDetailsDTO.getStartDate() != null && periodDetailsDTO.getEndDate() != null &&
                periodDetailsDTO.getStartDate().isAfter(periodDetailsDTO.getEndDate())) {
            throw new BadRequestException("Start date cannot be after end date.");
        }

        // Check for duplicate name if name is changed
        if (!periodToUpdate.getName().equals(periodDetailsDTO.getName())) {
            periodRepository.findByName(periodDetailsDTO.getName())
                    .ifPresent(p -> {
                        if (!p.getPeriodID().equals(id)) {
                            throw new BadRequestException("Evaluation period with name '" + periodDetailsDTO.getName() + "' already exists.");
                        }
                    });
        }

        periodToUpdate.setName(periodDetailsDTO.getName());
        periodToUpdate.setStartDate(periodDetailsDTO.getStartDate());
        periodToUpdate.setEndDate(periodDetailsDTO.getEndDate());
        periodToUpdate.setIsActive(periodDetailsDTO.getIsActive());

        EvaluationPeriod updatedPeriod = periodRepository.save(periodToUpdate);
        return convertToDto(updatedPeriod);
    }

    @Override
    public void deleteEvaluationPeriod(Integer id) {
        if (!periodRepository.existsById(id)) {
            throw new ResourceNotFoundException("Evaluation period not found with ID: " + id);
        }
        periodRepository.deleteById(id);
    }
}