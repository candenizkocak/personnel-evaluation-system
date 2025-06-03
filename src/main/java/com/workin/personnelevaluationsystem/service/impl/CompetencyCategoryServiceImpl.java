package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.CompetencyCategoryDTO;
import com.workin.personnelevaluationsystem.dto.CompetencyDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.Competency;
import com.workin.personnelevaluationsystem.model.CompetencyCategory;
import com.workin.personnelevaluationsystem.model.CompetencyLevel;
import com.workin.personnelevaluationsystem.repository.CompetencyCategoryRepository;
import com.workin.personnelevaluationsystem.repository.CompetencyLevelRepository; // Needed for nested mapping
import com.workin.personnelevaluationsystem.repository.CompetencyRepository; // Needed for nested mapping
import com.workin.personnelevaluationsystem.service.CompetencyCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompetencyCategoryServiceImpl implements CompetencyCategoryService {

    private final CompetencyCategoryRepository categoryRepository;
    private final CompetencyRepository competencyRepository; // For nested DTOs
    private final CompetencyLevelRepository competencyLevelRepository; // For nested DTOs

    @Autowired
    public CompetencyCategoryServiceImpl(CompetencyCategoryRepository categoryRepository,
                                         CompetencyRepository competencyRepository,
                                         CompetencyLevelRepository competencyLevelRepository) {
        this.categoryRepository = categoryRepository;
        this.competencyRepository = competencyRepository;
        this.competencyLevelRepository = competencyLevelRepository;
    }

    private CompetencyCategoryDTO convertToDto(CompetencyCategory category) {
        if (category == null) return null;
        List<CompetencyDTO> competencyDTOs = category.getCompetencies() != null ?
                category.getCompetencies().stream()
                        .map(this::convertCompetencyToDto) // Recursive call for nested
                        .collect(Collectors.toList()) : new ArrayList<>();

        return CompetencyCategoryDTO.builder()
                .categoryID(category.getCategoryID())
                .name(category.getName())
                .description(category.getDescription())
                .competencies(competencyDTOs)
                .build();
    }

    // Helper for nested Competency -> CompetencyDTO conversion
    private CompetencyDTO convertCompetencyToDto(Competency competency) {
        if (competency == null) return null;
        return CompetencyDTO.builder()
                .competencyID(competency.getCompetencyID())
                .name(competency.getName())
                .description(competency.getDescription())
                .categoryID(competency.getCategory() != null ? competency.getCategory().getCategoryID() : null)
                .levels(competency.getLevels().stream()
                        .map(this::convertLevelToDto)
                        .collect(Collectors.toList()))
                .build();
    }

    // Helper for nested CompetencyLevel -> CompetencyLevelDTO conversion
    private com.workin.personnelevaluationsystem.dto.CompetencyLevelDTO convertLevelToDto(CompetencyLevel level) {
        if (level == null) return null;
        return com.workin.personnelevaluationsystem.dto.CompetencyLevelDTO.builder()
                .levelID(level.getLevelID())
                .level(level.getLevel())
                .description(level.getDescription())
                .build();
    }


    // Helper to convert CompetencyCategoryDTO to Entity
    private CompetencyCategory convertToEntity(CompetencyCategoryDTO categoryDTO) {
        if (categoryDTO == null) return null;

        CompetencyCategory category = CompetencyCategory.builder()
                .categoryID(categoryDTO.getCategoryID())
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .build();

        if (categoryDTO.getCompetencies() != null && !categoryDTO.getCompetencies().isEmpty()) {
            categoryDTO.getCompetencies().stream()
                    .map(cDto -> convertCompetencyDtoToEntity(cDto, category))
                    .forEach(category::addCompetency); // Add to entity's collection
        }
        return category;
    }

    // Helper for nested CompetencyDTO -> Competency conversion
    private Competency convertCompetencyDtoToEntity(CompetencyDTO competencyDTO, CompetencyCategory category) {
        if (competencyDTO == null) return null;

        Competency competency = Competency.builder()
                .competencyID(competencyDTO.getCompetencyID())
                .name(competencyDTO.getName())
                .description(competencyDTO.getDescription())
                .category(category) // Set parent category
                .build();

        if (competencyDTO.getLevels() != null && !competencyDTO.getLevels().isEmpty()) {
            competencyDTO.getLevels().stream()
                    .map(lDto -> convertLevelDtoToEntity(lDto, competency))
                    .forEach(competency::addLevel); // Add to entity's collection
        }
        return competency;
    }

    // Helper for nested CompetencyLevelDTO -> CompetencyLevel conversion
    private CompetencyLevel convertLevelDtoToEntity(com.workin.personnelevaluationsystem.dto.CompetencyLevelDTO levelDTO, Competency competency) {
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
    public CompetencyCategoryDTO createCompetencyCategory(CompetencyCategoryDTO categoryDTO) {
        if (categoryRepository.findByName(categoryDTO.getName()).isPresent()) {
            throw new BadRequestException("Competency category with name '" + categoryDTO.getName() + "' already exists.");
        }
        CompetencyCategory category = convertToEntity(categoryDTO);
        CompetencyCategory savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    @Override
    public Optional<CompetencyCategoryDTO> getCompetencyCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    public List<CompetencyCategoryDTO> getAllCompetencyCategories() {
        return categoryRepository.findByOrderByNameAsc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompetencyCategoryDTO updateCompetencyCategory(Integer id, CompetencyCategoryDTO categoryDetailsDTO) {
        CompetencyCategory categoryToUpdate = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Competency category not found with ID: " + id));

        if (!categoryToUpdate.getName().equals(categoryDetailsDTO.getName())) {
            categoryRepository.findByName(categoryDetailsDTO.getName())
                    .ifPresent(c -> {
                        if (!c.getCategoryID().equals(id)) {
                            throw new BadRequestException("Competency category with name '" + categoryDetailsDTO.getName() + "' already exists.");
                        }
                    });
        }

        categoryToUpdate.setName(categoryDetailsDTO.getName());
        categoryToUpdate.setDescription(categoryDetailsDTO.getDescription());

        // Handle nested competencies update (clear and re-add)
        categoryToUpdate.getCompetencies().clear();
        if (categoryDetailsDTO.getCompetencies() != null) {
            categoryDetailsDTO.getCompetencies().forEach(cDto -> {
                Competency competency = convertCompetencyDtoToEntity(cDto, categoryToUpdate);
                categoryToUpdate.addCompetency(competency);
            });
        }

        CompetencyCategory updatedCategory = categoryRepository.save(categoryToUpdate);
        return convertToDto(updatedCategory);
    }

    @Override
    public void deleteCompetencyCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Competency category not found with ID: " + id);
        }
        // Deleting a category with cascade.ALL will also delete associated competencies and levels.
        // Be mindful of potential DataIntegrityViolation if linked by EmployeeCompetencies.
        categoryRepository.deleteById(id);
    }
}