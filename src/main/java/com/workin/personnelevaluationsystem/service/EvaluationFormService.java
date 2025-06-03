package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.EvaluationFormDTO;

import java.util.List;
import java.util.Optional;

public interface EvaluationFormService {
    EvaluationFormDTO createEvaluationForm(EvaluationFormDTO formDTO);
    Optional<EvaluationFormDTO> getEvaluationFormById(Integer id);
    List<EvaluationFormDTO> getAllEvaluationForms();
    EvaluationFormDTO updateEvaluationForm(Integer id, EvaluationFormDTO formDetailsDTO);
    void deleteEvaluationForm(Integer id);
}