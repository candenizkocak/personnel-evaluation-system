package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.EvaluationPeriodDTO;

import java.util.List;
import java.util.Optional;

public interface EvaluationPeriodService {
    EvaluationPeriodDTO createEvaluationPeriod(EvaluationPeriodDTO periodDTO);
    Optional<EvaluationPeriodDTO> getEvaluationPeriodById(Integer id);
    List<EvaluationPeriodDTO> getAllEvaluationPeriods();
    EvaluationPeriodDTO updateEvaluationPeriod(Integer id, EvaluationPeriodDTO periodDetailsDTO);
    void deleteEvaluationPeriod(Integer id);
}