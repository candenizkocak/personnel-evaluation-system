package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.QuestionTypeDTO;

import java.util.List;
import java.util.Optional;

public interface QuestionTypeService {
    QuestionTypeDTO createQuestionType(QuestionTypeDTO questionTypeDTO);
    Optional<QuestionTypeDTO> getQuestionTypeById(Integer id);
    List<QuestionTypeDTO> getAllQuestionTypes();
    QuestionTypeDTO updateQuestionType(Integer id, QuestionTypeDTO questionTypeDetailsDTO);
    void deleteQuestionType(Integer id);
}