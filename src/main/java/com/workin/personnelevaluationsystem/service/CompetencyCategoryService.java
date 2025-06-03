package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.CompetencyCategoryDTO;

import java.util.List;
import java.util.Optional;

public interface CompetencyCategoryService {
    CompetencyCategoryDTO createCompetencyCategory(CompetencyCategoryDTO categoryDTO);
    Optional<CompetencyCategoryDTO> getCompetencyCategoryById(Integer id);
    List<CompetencyCategoryDTO> getAllCompetencyCategories();
    CompetencyCategoryDTO updateCompetencyCategory(Integer id, CompetencyCategoryDTO categoryDetailsDTO);
    void deleteCompetencyCategory(Integer id);
}