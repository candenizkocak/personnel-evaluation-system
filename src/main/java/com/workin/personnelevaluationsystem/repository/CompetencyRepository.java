package com.workin.personnelevaluationsystem.repository;

import com.workin.personnelevaluationsystem.model.Competency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetencyRepository extends JpaRepository<Competency, Integer> {
    Optional<Competency> findByName(String name);
    List<Competency> findByCategory_CategoryIDOrderByNameAsc(Integer categoryId); // Find competencies by category
}