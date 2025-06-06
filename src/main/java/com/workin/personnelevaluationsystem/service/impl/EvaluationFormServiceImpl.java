package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.EvaluationFormDTO;
import com.workin.personnelevaluationsystem.dto.EvaluationQuestionDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.EvaluationForm;
import com.workin.personnelevaluationsystem.model.EvaluationQuestion;
import com.workin.personnelevaluationsystem.model.EvaluationType;
import com.workin.personnelevaluationsystem.repository.EvaluationFormRepository;
import com.workin.personnelevaluationsystem.repository.EvaluationTypeRepository;
import com.workin.personnelevaluationsystem.service.EvaluationFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EvaluationFormServiceImpl implements EvaluationFormService {

    private final EvaluationFormRepository formRepository;
    private final EvaluationTypeRepository evaluationTypeRepository;

    @Autowired
    public EvaluationFormServiceImpl(EvaluationFormRepository formRepository,
                                     EvaluationTypeRepository evaluationTypeRepository) {
        this.formRepository = formRepository;
        this.evaluationTypeRepository = evaluationTypeRepository;
    }

    // Helper to convert EvaluationForm Entity to DTO (now includes questions for display)
    private EvaluationFormDTO convertToDto(EvaluationForm form) {
        if (form == null) return null;
        List<EvaluationQuestionDTO> questionDTOs = form.getQuestions() != null ?
                form.getQuestions().stream()
                        .map(this::convertQuestionToDto)
                        .collect(Collectors.toList()) : new ArrayList<>();

        return EvaluationFormDTO.builder()
                .formID(form.getFormID())
                .title(form.getTitle())
                .description(form.getDescription())
                .typeID(form.getEvaluationType() != null ? form.getEvaluationType().getTypeID() : null)
                .isActive(form.getIsActive())
                .questions(questionDTOs) // Set the questions for display
                .build();
    }

    // Helper to convert nested EvaluationQuestion Entity to DTO
    private EvaluationQuestionDTO convertQuestionToDto(EvaluationQuestion question) {
        if (question == null) return null;
        return EvaluationQuestionDTO.builder()
                .questionID(question.getQuestionID())
                .questionText(question.getQuestionText())
                .questionTypeID(question.getQuestionType() != null ? question.getQuestionType().getQuestionTypeID() : null)
                .weight(question.getWeight())
                .isRequired(question.getIsRequired())
                .orderIndex(question.getOrderIndex())
                .build();
    }


    @Override
    @Transactional
    public EvaluationFormDTO createEvaluationForm(EvaluationFormDTO formDTO) {
        if (formRepository.findByTitle(formDTO.getTitle()).isPresent()) {
            throw new BadRequestException("Evaluation form with title '" + formDTO.getTitle() + "' already exists.");
        }

        EvaluationType evaluationType = evaluationTypeRepository.findById(formDTO.getTypeID())
                .orElseThrow(() -> new BadRequestException("Evaluation Type not found with ID: " + formDTO.getTypeID()));

        EvaluationForm form = EvaluationForm.builder()
                .title(formDTO.getTitle())
                .description(formDTO.getDescription())
                .evaluationType(evaluationType)
                .isActive(formDTO.getIsActive())
                .build();

        EvaluationForm savedForm = formRepository.save(form);
        return convertToDto(savedForm);
    }

    @Override
    public Optional<EvaluationFormDTO> getEvaluationFormById(Integer id) {
        return formRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<EvaluationFormDTO> getAllEvaluationForms() {
        return formRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EvaluationFormDTO updateEvaluationForm(Integer id, EvaluationFormDTO formDetailsDTO) {
        EvaluationForm formToUpdate = formRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation form not found with ID: " + id));

        if (!formToUpdate.getTitle().equals(formDetailsDTO.getTitle())) {
            formRepository.findByTitle(formDetailsDTO.getTitle())
                    .ifPresent(f -> {
                        if (!f.getFormID().equals(id)) {
                            throw new BadRequestException("Evaluation form with title '" + formDetailsDTO.getTitle() + "' already exists.");
                        }
                    });
        }

        formToUpdate.setTitle(formDetailsDTO.getTitle());
        formToUpdate.setDescription(formDetailsDTO.getDescription());
        formToUpdate.setIsActive(formDetailsDTO.getIsActive());

        if (formDetailsDTO.getTypeID() != null) {
            EvaluationType newType = evaluationTypeRepository.findById(formDetailsDTO.getTypeID())
                    .orElseThrow(() -> new BadRequestException("Evaluation Type not found with ID: " + formDetailsDTO.getTypeID()));
            formToUpdate.setEvaluationType(newType);
        }

        EvaluationForm updatedForm = formRepository.save(formToUpdate);
        return convertToDto(updatedForm);
    }

    @Override
    public void deleteEvaluationForm(Integer id) {
        if (!formRepository.existsById(id)) {
            throw new ResourceNotFoundException("Evaluation form not found with ID: " + id);
        }
        formRepository.deleteById(id);
    }
}