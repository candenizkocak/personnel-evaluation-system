package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.EvaluationQuestionDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.EvaluationForm;
import com.workin.personnelevaluationsystem.model.EvaluationQuestion;
import com.workin.personnelevaluationsystem.model.QuestionType;
import com.workin.personnelevaluationsystem.repository.EvaluationFormRepository;
import com.workin.personnelevaluationsystem.repository.EvaluationQuestionRepository;
import com.workin.personnelevaluationsystem.repository.QuestionTypeRepository;
import com.workin.personnelevaluationsystem.service.EvaluationQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EvaluationQuestionServiceImpl implements EvaluationQuestionService {

    private final EvaluationQuestionRepository questionRepository;
    private final EvaluationFormRepository formRepository;
    private final QuestionTypeRepository questionTypeRepository;

    @Autowired
    public EvaluationQuestionServiceImpl(EvaluationQuestionRepository questionRepository,
                                         EvaluationFormRepository formRepository,
                                         QuestionTypeRepository questionTypeRepository) {
        this.questionRepository = questionRepository;
        this.formRepository = formRepository;
        this.questionTypeRepository = questionTypeRepository;
    }

    private EvaluationQuestionDTO convertToDto(EvaluationQuestion question) {
        if (question == null) return null;
        return EvaluationQuestionDTO.builder()
                .questionID(question.getQuestionID())
                .questionText(question.getQuestionText())
                .questionTypeID(question.getQuestionType() != null ? question.getQuestionType().getQuestionTypeID() : null)
                .weight(question.getWeight())
                .isRequired(question.getIsRequired())
                .orderIndex(question.getOrderIndex())
                // formID is usually not returned in nested DTOs, but could be added here for direct question endpoint
                .build();
    }

    // Helper to convert DTO to Entity for a specific form
    private EvaluationQuestion convertToEntity(Integer formId, EvaluationQuestionDTO questionDTO) {
        if (questionDTO == null) return null;

        EvaluationForm evaluationForm = formRepository.findById(formId)
                .orElseThrow(() -> new BadRequestException("Evaluation Form not found with ID: " + formId));

        QuestionType questionType = questionTypeRepository.findById(questionDTO.getQuestionTypeID())
                .orElseThrow(() -> new BadRequestException("Question Type not found with ID: " + questionDTO.getQuestionTypeID()));

        return EvaluationQuestion.builder()
                .questionID(questionDTO.getQuestionID()) // Can be null for new questions
                .questionText(questionDTO.getQuestionText())
                .questionType(questionType)
                .weight(questionDTO.getWeight())
                .isRequired(questionDTO.getIsRequired())
                .orderIndex(questionDTO.getOrderIndex())
                .evaluationForm(evaluationForm)
                .build();
    }

    @Override
    @Transactional
    public EvaluationQuestionDTO createEvaluationQuestion(Integer formId, EvaluationQuestionDTO questionDTO) {
        // Validation: Ensure orderIndex is unique for the given form
        // (More complex: check existing questions for this form and their orderIndex)
        // For simplicity, we skip checking uniqueness of orderIndex here, relying on DB if it has unique constraint.

        EvaluationQuestion question = convertToEntity(formId, questionDTO);
        EvaluationQuestion savedQuestion = questionRepository.save(question);
        return convertToDto(savedQuestion);
    }

    @Override
    public Optional<EvaluationQuestionDTO> getEvaluationQuestionById(Integer id) {
        return questionRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<EvaluationQuestionDTO> getAllEvaluationQuestionsByFormId(Integer formId) {
        if (!formRepository.existsById(formId)) {
            throw new ResourceNotFoundException("Evaluation Form not found with ID: " + formId);
        }
        return questionRepository.findByEvaluationForm_FormIDOrderByOrderIndexAsc(formId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EvaluationQuestionDTO updateEvaluationQuestion(Integer id, EvaluationQuestionDTO questionDetailsDTO) {
        EvaluationQuestion questionToUpdate = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation question not found with ID: " + id));

        // Update attributes from DTO
        questionToUpdate.setQuestionText(questionDetailsDTO.getQuestionText());
        questionToUpdate.setWeight(questionDetailsDTO.getWeight());
        questionToUpdate.setIsRequired(questionDetailsDTO.getIsRequired());
        questionToUpdate.setOrderIndex(questionDetailsDTO.getOrderIndex());

        // Update QuestionType relationship
        if (questionDetailsDTO.getQuestionTypeID() != null) {
            QuestionType newQuestionType = questionTypeRepository.findById(questionDetailsDTO.getQuestionTypeID())
                    .orElseThrow(() -> new BadRequestException("Question Type not found with ID: " + questionDetailsDTO.getQuestionTypeID()));
            questionToUpdate.setQuestionType(newQuestionType);
        } else {
            questionToUpdate.setQuestionType(null); // Clear association if ID is null
        }

        // Note: Changing the formID for an existing question directly via this update method
        // is not straightforward with @OneToMany mappedBy relationships and usually requires
        // manipulation through the parent form's collection or a dedicated re-parenting logic.
        // For now, we assume questions remain with their original form.

        EvaluationQuestion updatedQuestion = questionRepository.save(questionToUpdate);
        return convertToDto(updatedQuestion);
    }

    @Override
    public void deleteEvaluationQuestion(Integer id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Evaluation question not found with ID: " + id);
        }
        questionRepository.deleteById(id);
    }
}