package com.workin.personnelevaluationsystem.repository;

import com.workin.personnelevaluationsystem.model.EvaluationPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EvaluationPeriodRepository extends JpaRepository<EvaluationPeriod, Integer> {
    Optional<EvaluationPeriod> findByName(String name); // Custom method
}