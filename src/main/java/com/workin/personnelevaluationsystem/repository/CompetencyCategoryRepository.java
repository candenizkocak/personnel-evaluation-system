package com.workin.personnelevaluationsystem.repository;

import com.workin.personnelevaluationsystem.model.CompetencyCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List; // Ensure this is imported

@Repository
public interface CompetencyCategoryRepository extends JpaRepository<CompetencyCategory, Integer> {
    Optional<CompetencyCategory> findByName(String name);
    List<CompetencyCategory> findByOrderByNameAsc();
}