package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.EvaluationQuestionDTO;

import java.util.List;
import java.util.Optional;

public interface EvaluationQuestionService {
    // Note: Creating/Updating questions directly without a form context can be tricky
    // as formID is mandatory. Typically done via parent form or specialized endpoints.
    // We'll provide a basic implementation for completeness.
    EvaluationQuestionDTO createEvaluationQuestion(Integer formId, EvaluationQuestionDTO questionDTO);
    Optional<EvaluationQuestionDTO> getEvaluationQuestionById(Integer id);
    List<EvaluationQuestionDTO> getAllEvaluationQuestionsByFormId(Integer formId);
    EvaluationQuestionDTO updateEvaluationQuestion(Integer id, EvaluationQuestionDTO questionDetailsDTO);
    void deleteEvaluationQuestion(Integer id);
}