package com.workin.personnelevaluationsystem.repository;

import com.workin.personnelevaluationsystem.model.CompetencyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetencyLevelRepository extends JpaRepository<CompetencyLevel, Integer> {
    Optional<CompetencyLevel> findByCompetency_CompetencyIDAndLevel(Integer competencyId, Integer level);
    List<CompetencyLevel> findByCompetency_CompetencyIDOrderByLevelAsc(Integer competencyId);
}