package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.CompetencyLevelDTO;

import java.util.List;
import java.util.Optional;

public interface CompetencyLevelService {
    CompetencyLevelDTO createCompetencyLevel(Integer competencyId, CompetencyLevelDTO levelDTO);
    Optional<CompetencyLevelDTO> getCompetencyLevelById(Integer id);
    List<CompetencyLevelDTO> getCompetencyLevelsByCompetencyId(Integer competencyId);
    List<CompetencyLevelDTO> getAllCompetencyLevels(); // <-- ADD THIS METHOD
    CompetencyLevelDTO updateCompetencyLevel(Integer id, CompetencyLevelDTO levelDetailsDTO);
    void deleteCompetencyLevel(Integer id);
}