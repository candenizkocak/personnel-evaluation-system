package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.QuestionTypeDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.QuestionType;
import com.workin.personnelevaluationsystem.repository.QuestionTypeRepository;
import com.workin.personnelevaluationsystem.service.QuestionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionTypeServiceImpl implements QuestionTypeService {

    private final QuestionTypeRepository questionTypeRepository;

    @Autowired
    public QuestionTypeServiceImpl(QuestionTypeRepository questionTypeRepository) {
        this.questionTypeRepository = questionTypeRepository;
    }

    private QuestionTypeDTO convertToDto(QuestionType questionType) {
        if (questionType == null) return null;
        return QuestionTypeDTO.builder()
                .questionTypeID(questionType.getQuestionTypeID())
                .name(questionType.getName())
                .description(questionType.getDescription())
                .build();
    }

    private QuestionType convertToEntity(QuestionTypeDTO questionTypeDTO) {
        if (questionTypeDTO == null) return null;
        return QuestionType.builder()
                .questionTypeID(questionTypeDTO.getQuestionTypeID())
                .name(questionTypeDTO.getName())
                .description(questionTypeDTO.getDescription())
                .build();
    }

    @Override
    public QuestionTypeDTO createQuestionType(QuestionTypeDTO questionTypeDTO) {
        if (questionTypeRepository.findByName(questionTypeDTO.getName()).isPresent()) {
            throw new BadRequestException("Question type with name '" + questionTypeDTO.getName() + "' already exists.");
        }
        QuestionType questionType = convertToEntity(questionTypeDTO);
        QuestionType savedQuestionType = questionTypeRepository.save(questionType);
        return convertToDto(savedQuestionType);
    }

    @Override
    public Optional<QuestionTypeDTO> getQuestionTypeById(Integer id) {
        return questionTypeRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<QuestionTypeDTO> getAllQuestionTypes() {
        return questionTypeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionTypeDTO updateQuestionType(Integer id, QuestionTypeDTO questionTypeDetailsDTO) {
        QuestionType questionTypeToUpdate = questionTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question type not found with ID: " + id));

        if (!questionTypeToUpdate.getName().equals(questionTypeDetailsDTO.getName())) {
            questionTypeRepository.findByName(questionTypeDetailsDTO.getName())
                    .ifPresent(qt -> {
                        if (!qt.getQuestionTypeID().equals(id)) {
                            throw new BadRequestException("Question type with name '" + questionTypeDetailsDTO.getName() + "' already exists.");
                        }
                    });
        }

        questionTypeToUpdate.setName(questionTypeDetailsDTO.getName());
        questionTypeToUpdate.setDescription(questionTypeDetailsDTO.getDescription());

        QuestionType updatedQuestionType = questionTypeRepository.save(questionTypeToUpdate);
        return convertToDto(updatedQuestionType);
    }

    @Override
    public void deleteQuestionType(Integer id) {
        if (!questionTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question type not found with ID: " + id);
        }
        // Add check for existing questions using this type before deleting.
        // If the type is referenced by EvaluationQuestions, deleting it directly will cause a DataIntegrityViolationException.
        questionTypeRepository.deleteById(id);
    }
}