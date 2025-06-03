package com.workin.personnelevaluationsystem.repository;

import com.workin.personnelevaluationsystem.model.EvaluationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationFormRepository extends JpaRepository<EvaluationForm, Integer> {
    Optional<EvaluationForm> findByTitle(String title); // Custom method
    List<EvaluationForm> findByEvaluationType_TypeID(Integer typeId); // Find forms by evaluation type
}