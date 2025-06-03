package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.CompetencyDTO;
import com.workin.personnelevaluationsystem.dto.CompetencyLevelDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.Competency;
import com.workin.personnelevaluationsystem.model.CompetencyCategory;
import com.workin.personnelevaluationsystem.model.CompetencyLevel;
import com.workin.personnelevaluationsystem.repository.CompetencyCategoryRepository;
import com.workin.personnelevaluationsystem.repository.CompetencyLevelRepository;
import com.workin.personnelevaluationsystem.repository.CompetencyRepository;
import com.workin.personnelevaluationsystem.service.CompetencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompetencyServiceImpl implements CompetencyService {

    private final CompetencyRepository competencyRepository;
    private final CompetencyCategoryRepository categoryRepository;
    private final CompetencyLevelRepository competencyLevelRepository; // For nested levels

    @Autowired
    public CompetencyServiceImpl(CompetencyRepository competencyRepository,
                                 CompetencyCategoryRepository categoryRepository,
                                 CompetencyLevelRepository competencyLevelRepository) {
        this.competencyRepository = competencyRepository;
        this.categoryRepository = categoryRepository;
        this.competencyLevelRepository = competencyLevelRepository;
    }

    private CompetencyDTO convertToDto(Competency competency) {
        if (competency == null) return null;
        List<CompetencyLevelDTO> levelDTOs = competency.getLevels() != null ?
                competency.getLevels().stream()
                        .map(this::convertLevelToDto)
                        .collect(Collectors.toList()) : new ArrayList<>();

        return CompetencyDTO.builder()
                .competencyID(competency.getCompetencyID())
                .name(competency.getName())
                .description(competency.getDescription())
                .categoryID(competency.getCategory() != null ? competency.getCategory().getCategoryID() : null)
                .levels(levelDTOs)
                .build();
    }

    // Helper for nested CompetencyLevel -> CompetencyLevelDTO conversion
    private CompetencyLevelDTO convertLevelToDto(CompetencyLevel level) {
        if (level == null) return null;
        return CompetencyLevelDTO.builder()
                .levelID(level.getLevelID())
                .level(level.getLevel())
                .description(level.getDescription())
                .build();
    }

    // Helper to convert CompetencyDTO to Entity
    private Competency convertToEntity(CompetencyDTO competencyDTO) {
        if (competencyDTO == null) return null;

        CompetencyCategory category = categoryRepository.findById(competencyDTO.getCategoryID())
                .orElseThrow(() -> new BadRequestException("Competency Category not found with ID: " + competencyDTO.getCategoryID()));

        Competency competency = Competency.builder()
                .competencyID(competencyDTO.getCompetencyID())
                .name(competencyDTO.getName())
                .description(competencyDTO.getDescription())
                .category(category)
                .build();

        if (competencyDTO.getLevels() != null && !competencyDTO.getLevels().isEmpty()) {
            // Validate uniqueness of levels within the DTO before converting
            List<Integer> distinctLevels = competencyDTO.getLevels().stream()
                    .map(CompetencyLevelDTO::getLevel)
                    .distinct()
                    .collect(Collectors.toList());
            if (distinctLevels.size() != competencyDTO.getLevels().size()) {
                throw new BadRequestException("Duplicate levels found within the competency levels list.");
            }

            competencyDTO.getLevels().stream()
                    .map(lDto -> convertLevelDtoToEntity(lDto, competency))
                    .forEach(competency::addLevel);
        }
        return competency;
    }

    // Helper for nested CompetencyLevelDTO -> CompetencyLevel conversion
    private CompetencyLevel convertLevelDtoToEntity(CompetencyLevelDTO levelDTO, Competency competency) {
        if (levelDTO == null) return null;

        return CompetencyLevel.builder()
                .levelID(levelDTO.getLevelID())
                .level(levelDTO.getLevel())
                .description(levelDTO.getDescription())
                .competency(competency) // Set parent competency
                .build();
    }

    @Override
    @Transactional
    public CompetencyDTO createCompetency(CompetencyDTO competencyDTO) {
        if (competencyRepository.findByName(competencyDTO.getName()).isPresent()) {
            throw new BadRequestException("Competency with name '" + competencyDTO.getName() + "' already exists.");
        }
        Competency competency = convertToEntity(competencyDTO);
        Competency savedCompetency = competencyRepository.save(competency);
        return convertToDto(savedCompetency);
    }

    @Override
    public Optional<CompetencyDTO> getCompetencyById(Integer id) {
        return competencyRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    public List<CompetencyDTO> getAllCompetencies() {
        return competencyRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompetencyDTO> getCompetenciesByCategoryId(Integer categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Competency category not found with ID: " + categoryId);
        }
        return competencyRepository.findByCategory_CategoryIDOrderByNameAsc(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompetencyDTO updateCompetency(Integer id, CompetencyDTO competencyDetailsDTO) {
        Competency competencyToUpdate = competencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Competency not found with ID: " + id));

        if (!competencyToUpdate.getName().equals(competencyDetailsDTO.getName())) {
            competencyRepository.findByName(competencyDetailsDTO.getName())
                    .ifPresent(c -> {
                        if (!c.getCompetencyID().equals(id)) {
                            throw new BadRequestException("Competency with name '" + competencyDetailsDTO.getName() + "' already exists.");
                        }
                    });
        }

        competencyToUpdate.setName(competencyDetailsDTO.getName());
        competencyToUpdate.setDescription(competencyDetailsDTO.getDescription());

        // Update category relationship
        if (competencyDetailsDTO.getCategoryID() != null) {
            CompetencyCategory newCategory = categoryRepository.findById(competencyDetailsDTO.getCategoryID())
                    .orElseThrow(() -> new BadRequestException("Competency Category not found with ID: " + competencyDetailsDTO.getCategoryID()));
            competencyToUpdate.setCategory(newCategory);
        } else {
            competencyToUpdate.setCategory(null);
        }

        // Handle nested levels update (clear and re-add)
        // This assumes the client sends the FULL list of levels for an update.
        competencyToUpdate.getLevels().clear();
        if (competencyDetailsDTO.getLevels() != null && !competencyDetailsDTO.getLevels().isEmpty()) {
            // Validate uniqueness of levels within the DTO before converting
            List<Integer> distinctLevels = competencyDetailsDTO.getLevels().stream()
                    .map(CompetencyLevelDTO::getLevel)
                    .distinct()
                    .collect(Collectors.toList());
            if (distinctLevels.size() != competencyDetailsDTO.getLevels().size()) {
                throw new BadRequestException("Duplicate levels found within the competency levels list for update.");
            }
            competencyDetailsDTO.getLevels().forEach(lDto -> {
                CompetencyLevel level = convertLevelDtoToEntity(lDto, competencyToUpdate);
                competencyToUpdate.addLevel(level);
            });
        }

        Competency updatedCompetency = competencyRepository.save(competencyToUpdate);
        return convertToDto(updatedCompetency);
    }

    @Override
    public void deleteCompetency(Integer id) {
        if (!competencyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Competency not found with ID: " + id);
        }
        // Deleting a competency with cascade.ALL will also delete associated levels.
        // Be mindful of potential DataIntegrityViolation if linked by EmployeeCompetencies.
        competencyRepository.deleteById(id);
    }
}