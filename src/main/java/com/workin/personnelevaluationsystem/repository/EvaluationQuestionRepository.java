package com.workin.personnelevaluationsystem.repository;

import com.workin.personnelevaluationsystem.model.EvaluationQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationQuestionRepository extends JpaRepository<EvaluationQuestion, Integer> {
    List<EvaluationQuestion> findByEvaluationForm_FormIDOrderByOrderIndexAsc(Integer formId); // Get questions for a specific form, ordered
}