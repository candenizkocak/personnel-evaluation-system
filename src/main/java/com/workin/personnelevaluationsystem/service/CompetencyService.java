package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.CompetencyDTO;

import java.util.List;
import java.util.Optional;

public interface CompetencyService {
    CompetencyDTO createCompetency(CompetencyDTO competencyDTO);
    Optional<CompetencyDTO> getCompetencyById(Integer id);
    List<CompetencyDTO> getAllCompetencies();
    List<CompetencyDTO> getCompetenciesByCategoryId(Integer categoryId);
    CompetencyDTO updateCompetency(Integer id, CompetencyDTO competencyDetailsDTO);
    void deleteCompetency(Integer id);
}