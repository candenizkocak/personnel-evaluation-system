package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.CompetencyLevelDTO;

import java.util.List;
import java.util.Optional;

public interface CompetencyLevelService {
    // Create/Update levels directly, often for adding to an existing competency
    CompetencyLevelDTO createCompetencyLevel(Integer competencyId, CompetencyLevelDTO levelDTO);
    Optional<CompetencyLevelDTO> getCompetencyLevelById(Integer id);
    List<CompetencyLevelDTO> getCompetencyLevelsByCompetencyId(Integer competencyId);
    CompetencyLevelDTO updateCompetencyLevel(Integer id, CompetencyLevelDTO levelDetailsDTO);
    void deleteCompetencyLevel(Integer id);
}