package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.EvaluationTypeDTO;

import java.util.List;
import java.util.Optional;

public interface EvaluationTypeService {
    EvaluationTypeDTO createEvaluationType(EvaluationTypeDTO typeDTO);
    Optional<EvaluationTypeDTO> getEvaluationTypeById(Integer id);
    List<EvaluationTypeDTO> getAllEvaluationTypes();
    EvaluationTypeDTO updateEvaluationType(Integer id, EvaluationTypeDTO typeDetailsDTO);
    void deleteEvaluationType(Integer id);
}