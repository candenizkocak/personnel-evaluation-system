package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.CompetencyLevelDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.Competency;
import com.workin.personnelevaluationsystem.model.CompetencyLevel;
import com.workin.personnelevaluationsystem.repository.CompetencyLevelRepository;
import com.workin.personnelevaluationsystem.repository.CompetencyRepository;
import com.workin.personnelevaluationsystem.service.CompetencyLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompetencyLevelServiceImpl implements CompetencyLevelService {

    private final CompetencyLevelRepository levelRepository;
    private final CompetencyRepository competencyRepository;

    @Autowired
    public CompetencyLevelServiceImpl(CompetencyLevelRepository levelRepository, CompetencyRepository competencyRepository) {
        this.levelRepository = levelRepository;
        this.competencyRepository = competencyRepository;
    }

    private CompetencyLevelDTO convertToDto(CompetencyLevel level) {
        if (level == null) return null;
        return CompetencyLevelDTO.builder()
                .levelID(level.getLevelID())
                .level(level.getLevel())
                .description(level.getDescription())
                // competencyID is not part of CompetencyLevelDTO when it's nested
                .build();
    }

    private CompetencyLevel convertToEntity(Integer competencyId, CompetencyLevelDTO levelDTO) {
        if (levelDTO == null) return null;

        Competency competency = competencyRepository.findById(competencyId)
                .orElseThrow(() -> new BadRequestException("Competency not found with ID: " + competencyId));

        // Check if level already exists for this competency
        if (levelRepository.findByCompetency_CompetencyIDAndLevel(competencyId, levelDTO.getLevel()).isPresent()) {
            throw new BadRequestException("Competency level " + levelDTO.getLevel() + " already exists for competency ID: " + competencyId);
        }

        return CompetencyLevel.builder()
                .levelID(levelDTO.getLevelID()) // Will be null for new creations
                .level(levelDTO.getLevel())
                .description(levelDTO.getDescription())
                .competency(competency)
                .build();
    }

    @Override
    @Transactional
    public CompetencyLevelDTO createCompetencyLevel(Integer competencyId, CompetencyLevelDTO levelDTO) {
        CompetencyLevel level = convertToEntity(competencyId, levelDTO);
        CompetencyLevel savedLevel = levelRepository.save(level);
        return convertToDto(savedLevel);
    }

    @Override
    public Optional<CompetencyLevelDTO> getCompetencyLevelById(Integer id) {
        return levelRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<CompetencyLevelDTO> getCompetencyLevelsByCompetencyId(Integer competencyId) {
        if (!competencyRepository.existsById(competencyId)) {
            throw new ResourceNotFoundException("Competency not found with ID: " + competencyId);
        }
        return levelRepository.findByCompetency_CompetencyIDOrderByLevelAsc(competencyId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompetencyLevelDTO updateCompetencyLevel(Integer id, CompetencyLevelDTO levelDetailsDTO) {
        CompetencyLevel levelToUpdate = levelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Competency level not found with ID: " + id));

        // Check if changing level to an existing one for the same competency
        if (!levelToUpdate.getLevel().equals(levelDetailsDTO.getLevel())) {
            levelRepository.findByCompetency_CompetencyIDAndLevel(
                            levelToUpdate.getCompetency().getCompetencyID(),
                            levelDetailsDTO.getLevel())
                    .ifPresent(existingLevel -> {
                        if (!existingLevel.getLevelID().equals(id)) { // If it's a different level object
                            throw new BadRequestException("Competency level " + levelDetailsDTO.getLevel() + " already exists for competency ID: " + levelToUpdate.getCompetency().getCompetencyID());
                        }
                    });
        }

        levelToUpdate.setLevel(levelDetailsDTO.getLevel());
        levelToUpdate.setDescription(levelDetailsDTO.getDescription());

        CompetencyLevel updatedLevel = levelRepository.save(levelToUpdate);
        return convertToDto(updatedLevel);
    }

    @Override
    public void deleteCompetencyLevel(Integer id) {
        if (!levelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Competency level not found with ID: " + id);
        }
        // Be mindful of DataIntegrityViolation if linked by EmployeeCompetencies.
        levelRepository.deleteById(id);
    }
}