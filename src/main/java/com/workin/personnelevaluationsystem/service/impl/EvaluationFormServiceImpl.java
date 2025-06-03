package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.EvaluationFormDTO;
import com.workin.personnelevaluationsystem.dto.EvaluationQuestionDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.EvaluationForm;
import com.workin.personnelevaluationsystem.model.EvaluationQuestion;
import com.workin.personnelevaluationsystem.model.EvaluationType;
import com.workin.personnelevaluationsystem.model.QuestionType;
import com.workin.personnelevaluationsystem.repository.EvaluationFormRepository;
import com.workin.personnelevaluationsystem.repository.EvaluationTypeRepository;
import com.workin.personnelevaluationsystem.repository.QuestionTypeRepository;
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
    private final QuestionTypeRepository questionTypeRepository;

    @Autowired
    public EvaluationFormServiceImpl(EvaluationFormRepository formRepository,
                                     EvaluationTypeRepository evaluationTypeRepository,
                                     QuestionTypeRepository questionTypeRepository) {
        this.formRepository = formRepository;
        this.evaluationTypeRepository = evaluationTypeRepository;
        this.questionTypeRepository = questionTypeRepository;
    }

    // Helper to convert EvaluationForm Entity to DTO
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
                .questions(questionDTOs)
                .build();
    }

    // Helper to convert EvaluationQuestion Entity to DTO
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

    // Helper to convert EvaluationFormDTO to Entity
    private EvaluationForm convertToEntity(EvaluationFormDTO formDTO) {
        if (formDTO == null) return null;

        // Fetch EvaluationType
        EvaluationType evaluationType = evaluationTypeRepository.findById(formDTO.getTypeID())
                .orElseThrow(() -> new BadRequestException("Evaluation Type not found with ID: " + formDTO.getTypeID()));

        EvaluationForm form = EvaluationForm.builder()
                .formID(formDTO.getFormID())
                .title(formDTO.getTitle())
                .description(formDTO.getDescription())
                .evaluationType(evaluationType)
                .isActive(formDTO.getIsActive())
                .build();

        // Handle nested questions
        if (formDTO.getQuestions() != null && !formDTO.getQuestions().isEmpty()) {
            List<EvaluationQuestion> questions = formDTO.getQuestions().stream()
                    .map(qDto -> convertQuestionDtoToEntity(qDto, form))
                    .collect(Collectors.toList());
            // Add questions using helper to ensure bi-directional link
            questions.forEach(form::addQuestion);
        }
        return form;
    }

    // Helper to convert EvaluationQuestionDTO to Entity
    private EvaluationQuestion convertQuestionDtoToEntity(EvaluationQuestionDTO questionDTO, EvaluationForm form) {
        if (questionDTO == null) return null;

        // Fetch QuestionType
        QuestionType questionType = questionTypeRepository.findById(questionDTO.getQuestionTypeID())
                .orElseThrow(() -> new BadRequestException("Question Type not found with ID: " + questionDTO.getQuestionTypeID()));

        return EvaluationQuestion.builder()
                .questionID(questionDTO.getQuestionID()) // Can be null for new questions
                .questionText(questionDTO.getQuestionText())
                .questionType(questionType)
                .weight(questionDTO.getWeight())
                .isRequired(questionDTO.getIsRequired())
                .orderIndex(questionDTO.getOrderIndex())
                .evaluationForm(form) // Set the parent form
                .build();
    }


    @Override
    @Transactional
    public EvaluationFormDTO createEvaluationForm(EvaluationFormDTO formDTO) {
        // Check for duplicate title
        if (formRepository.findByTitle(formDTO.getTitle()).isPresent()) {
            throw new BadRequestException("Evaluation form with title '" + formDTO.getTitle() + "' already exists.");
        }

        EvaluationForm form = convertToEntity(formDTO);
        EvaluationForm savedForm = formRepository.save(form); // Questions are cascaded and saved with the form
        return convertToDto(savedForm);
    }

    @Override
    public Optional<EvaluationFormDTO> getEvaluationFormById(Integer id) {
        return formRepository.findById(id)
                .map(this::convertToDto);
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

        // Check for duplicate title if name is changed
        if (!formToUpdate.getTitle().equals(formDetailsDTO.getTitle())) {
            formRepository.findByTitle(formDetailsDTO.getTitle())
                    .ifPresent(f -> {
                        if (!f.getFormID().equals(id)) {
                            throw new BadRequestException("Evaluation form with title '" + formDetailsDTO.getTitle() + "' already exists.");
                        }
                    });
        }

        // Update simple properties
        formToUpdate.setTitle(formDetailsDTO.getTitle());
        formToUpdate.setDescription(formDetailsDTO.getDescription());
        formToUpdate.setIsActive(formDetailsDTO.getIsActive());

        // Update EvaluationType
        if (formDetailsDTO.getTypeID() != null) {
            EvaluationType newType = evaluationTypeRepository.findById(formDetailsDTO.getTypeID())
                    .orElseThrow(() -> new BadRequestException("Evaluation Type not found with ID: " + formDetailsDTO.getTypeID()));
            formToUpdate.setEvaluationType(newType);
        } else {
            formToUpdate.setEvaluationType(null);
        }

        // Handle nested questions update
        // This is a common pattern for managing @OneToMany relationships for full replacement
        // Clear existing questions and add new ones (requires orphanRemoval=true)
        formToUpdate.getQuestions().clear();
        if (formDetailsDTO.getQuestions() != null) {
            formDetailsDTO.getQuestions().forEach(qDto -> {
                EvaluationQuestion question = convertQuestionDtoToEntity(qDto, formToUpdate);
                formToUpdate.addQuestion(question); // Re-adds and sets parent reference
            });
        }

        EvaluationForm updatedForm = formRepository.save(formToUpdate);
        return convertToDto(updatedForm);
    }

    @Override
    public void deleteEvaluationForm(Integer id) {
        if (!formRepository.existsById(id)) {
            throw new ResourceNotFoundException("Evaluation form not found with ID: " + id);
        }
        // Note: If a form is referenced by PerformanceReviews, deleting it will throw a DataIntegrityViolationException.
        formRepository.deleteById(id);
    }
}