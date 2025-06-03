package com.workin.personnelevaluationsystem.repository;

import com.workin.personnelevaluationsystem.model.EvaluationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EvaluationTypeRepository extends JpaRepository<EvaluationType, Integer> {
    Optional<EvaluationType> findByName(String name); // Custom method
}