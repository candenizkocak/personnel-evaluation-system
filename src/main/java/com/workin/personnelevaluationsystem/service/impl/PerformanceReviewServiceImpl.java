package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.PerformanceReviewCreateDTO;
import com.workin.personnelevaluationsystem.dto.PerformanceReviewResponseDTO;
import com.workin.personnelevaluationsystem.dto.ReviewResponseDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.*;
import com.workin.personnelevaluationsystem.repository.*;
import com.workin.personnelevaluationsystem.service.PerformanceReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PerformanceReviewServiceImpl implements PerformanceReviewService {

    private final PerformanceReviewRepository reviewRepository;
    private final EmployeeRepository employeeRepository;
    private final EvaluationPeriodRepository periodRepository;
    private final EvaluationFormRepository formRepository;
    private final EvaluationQuestionRepository questionRepository; // Needed for response validation

    @Autowired
    public PerformanceReviewServiceImpl(PerformanceReviewRepository reviewRepository,
                                        EmployeeRepository employeeRepository,
                                        EvaluationPeriodRepository periodRepository,
                                        EvaluationFormRepository formRepository,
                                        EvaluationQuestionRepository questionRepository) {
        this.reviewRepository = reviewRepository;
        this.employeeRepository = employeeRepository;
        this.periodRepository = periodRepository;
        this.formRepository = formRepository;
        this.questionRepository = questionRepository;
    }

    // Helper to convert ReviewResponse Entity to DTO
    private ReviewResponseDTO convertResponseToDto(ReviewResponse response) {
        if (response == null) return null;
        return ReviewResponseDTO.builder()
                .responseID(response.getResponseID())
                .questionID(response.getEvaluationQuestion() != null ? response.getEvaluationQuestion().getQuestionID() : null)
                .responseText(response.getResponseText())
                .numericResponse(response.getNumericResponse())
                .build();
    }

    // Helper to convert PerformanceReview Entity to Response DTO
    private PerformanceReviewResponseDTO convertToResponseDto(PerformanceReview review) {
        if (review == null) return null;

        List<ReviewResponseDTO> responseDTOs = review.getReviewResponses() != null ?
                review.getReviewResponses().stream()
                        .map(this::convertResponseToDto)
                        .collect(Collectors.toList()) : new ArrayList<>();

        return PerformanceReviewResponseDTO.builder()
                .reviewID(review.getReviewID())
                .employeeID(review.getEmployee() != null ? review.getEmployee().getEmployeeID() : null)
                .employeeFullName(review.getEmployee() != null ?
                        review.getEmployee().getFirstName() + " " + review.getEmployee().getLastName() : null)
                .evaluatorID(review.getEvaluator() != null ? review.getEvaluator().getEmployeeID() : null)
                .evaluatorFullName(review.getEvaluator() != null ?
                        review.getEvaluator().getFirstName() + " " + review.getEvaluator().getLastName() : null)
                .periodID(review.getEvaluationPeriod() != null ? review.getEvaluationPeriod().getPeriodID() : null)
                .periodName(review.getEvaluationPeriod() != null ? review.getEvaluationPeriod().getName() : null)
                .formID(review.getEvaluationForm() != null ? review.getEvaluationForm().getFormID() : null)
                .formTitle(review.getEvaluationForm() != null ? review.getEvaluationForm().getTitle() : null)
                .submissionDate(review.getSubmissionDate())
                .status(review.getStatus())
                .finalScore(review.getFinalScore())
                .reviewResponses(responseDTOs)
                .build();
    }

    // Helper to convert Create DTO to PerformanceReview Entity
    private PerformanceReview convertToEntity(PerformanceReviewCreateDTO reviewDTO) {
        if (reviewDTO == null) return null;

        Employee employee = employeeRepository.findById(reviewDTO.getEmployeeID())
                .orElseThrow(() -> new BadRequestException("Employee being reviewed not found with ID: " + reviewDTO.getEmployeeID()));
        Employee evaluator = employeeRepository.findById(reviewDTO.getEvaluatorID())
                .orElseThrow(() -> new BadRequestException("Evaluator not found with ID: " + reviewDTO.getEvaluatorID()));
        EvaluationPeriod period = periodRepository.findById(reviewDTO.getPeriodID())
                .orElseThrow(() -> new BadRequestException("Evaluation Period not found with ID: " + reviewDTO.getPeriodID()));
        EvaluationForm form = formRepository.findById(reviewDTO.getFormID())
                .orElseThrow(() -> new BadRequestException("Evaluation Form not found with ID: " + reviewDTO.getFormID()));

        // Business rule: An employee cannot evaluate themselves
        if (employee.getEmployeeID().equals(evaluator.getEmployeeID())) {
            throw new BadRequestException("An employee cannot evaluate themselves.");
        }

        PerformanceReview review = PerformanceReview.builder()
                .reviewID(reviewDTO.getReviewID())
                .employee(employee)
                .evaluator(evaluator)
                .evaluationPeriod(period)
                .evaluationForm(form)
                .submissionDate(reviewDTO.getSubmissionDate() != null ? reviewDTO.getSubmissionDate() : LocalDateTime.now())
                .status(reviewDTO.getStatus() != null ? reviewDTO.getStatus() : "Draft") // Default to Draft if not provided
                .finalScore(reviewDTO.getFinalScore())
                .build();

        // Handle nested responses
        if (reviewDTO.getReviewResponses() != null && !reviewDTO.getReviewResponses().isEmpty()) {
            // Get all valid questions for the given form to validate responses
            Set<Integer> validQuestionIdsForForm = questionRepository
                    .findByEvaluationForm_FormIDOrderByOrderIndexAsc(form.getFormID())
                    .stream()
                    .map(EvaluationQuestion::getQuestionID)
                    .collect(Collectors.toSet());

            for (ReviewResponseDTO responseDTO : reviewDTO.getReviewResponses()) {
                // Validate if the question ID exists and belongs to the specified form
                if (!validQuestionIdsForForm.contains(responseDTO.getQuestionID())) {
                    throw new BadRequestException("Question ID " + responseDTO.getQuestionID() + " is not valid for Evaluation Form ID " + form.getFormID());
                }
                EvaluationQuestion question = questionRepository.findById(responseDTO.getQuestionID())
                        .orElseThrow(() -> new BadRequestException("Question not found with ID: " + responseDTO.getQuestionID())); // Should not happen if validQuestionIdsForForm check passes

                ReviewResponse response = ReviewResponse.builder()
                        .responseID(responseDTO.getResponseID()) // Will be null for new responses
                        .evaluationQuestion(question)
                        .responseText(responseDTO.getResponseText())
                        .numericResponse(responseDTO.getNumericResponse())
                        .performanceReview(review) // Set parent review
                        .build();
                review.addReviewResponse(response); // Add to collection, sets bi-directional
            }
        }
        return review;
    }

    @Override
    @Transactional
    public PerformanceReviewResponseDTO createPerformanceReview(PerformanceReviewCreateDTO reviewDTO) {
        PerformanceReview review = convertToEntity(reviewDTO); // Handles relationship validation
        PerformanceReview savedReview = reviewRepository.save(review); // Responses are cascaded and saved
        return convertToResponseDto(savedReview);
    }

    @Override
    public Optional<PerformanceReviewResponseDTO> getPerformanceReviewById(Integer id) {
        return reviewRepository.findById(id).map(this::convertToResponseDto);
    }

    @Override
    public List<PerformanceReviewResponseDTO> getAllPerformanceReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PerformanceReviewResponseDTO updatePerformanceReview(Integer id, PerformanceReviewCreateDTO reviewDetailsDTO) {
        PerformanceReview reviewToUpdate = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance review not found with ID: " + id));

        // Update relationships and simple fields if provided in DTO
        if (reviewDetailsDTO.getEmployeeID() != null) {
            Employee employee = employeeRepository.findById(reviewDetailsDTO.getEmployeeID())
                    .orElseThrow(() -> new BadRequestException("Employee being reviewed not found with ID: " + reviewDetailsDTO.getEmployeeID()));
            reviewToUpdate.setEmployee(employee);
        }
        if (reviewDetailsDTO.getEvaluatorID() != null) {
            Employee evaluator = employeeRepository.findById(reviewDetailsDTO.getEvaluatorID())
                    .orElseThrow(() -> new BadRequestException("Evaluator not found with ID: " + reviewDetailsDTO.getEvaluatorID()));
            reviewToUpdate.setEvaluator(evaluator);
        }
        if (reviewDetailsDTO.getPeriodID() != null) {
            EvaluationPeriod period = periodRepository.findById(reviewDetailsDTO.getPeriodID())
                    .orElseThrow(() -> new BadRequestException("Evaluation Period not found with ID: " + reviewDetailsDTO.getPeriodID()));
            reviewToUpdate.setEvaluationPeriod(period);
        }
        if (reviewDetailsDTO.getFormID() != null) {
            EvaluationForm form = formRepository.findById(reviewDetailsDTO.getFormID())
                    .orElseThrow(() -> new BadRequestException("Evaluation Form not found with ID: " + reviewDetailsDTO.getFormID()));
            reviewToUpdate.setEvaluationForm(form);
        }

        // Re-validate self-evaluation rule after potential ID updates
        if (reviewToUpdate.getEmployee() != null && reviewToUpdate.getEvaluator() != null &&
                reviewToUpdate.getEmployee().getEmployeeID().equals(reviewToUpdate.getEvaluator().getEmployeeID())) {
            throw new BadRequestException("An employee cannot evaluate themselves.");
        }

        reviewToUpdate.setSubmissionDate(reviewDetailsDTO.getSubmissionDate() != null ? reviewDetailsDTO.getSubmissionDate() : reviewToUpdate.getSubmissionDate());
        reviewToUpdate.setStatus(reviewDetailsDTO.getStatus() != null ? reviewDetailsDTO.getStatus() : reviewToUpdate.getStatus());
        reviewToUpdate.setFinalScore(reviewDetailsDTO.getFinalScore() != null ? reviewDetailsDTO.getFinalScore() : reviewToUpdate.getFinalScore());

        // Handle nested responses update
        // Clear existing responses and add new ones (requires orphanRemoval=true)
        // This assumes the client sends the FULL list of responses for an update.
        // If partial updates are needed, more complex logic is required (e.g., merging lists).
        reviewToUpdate.getReviewResponses().clear();
        if (reviewDetailsDTO.getReviewResponses() != null && !reviewDetailsDTO.getReviewResponses().isEmpty()) {
            Set<Integer> validQuestionIdsForForm = questionRepository
                    .findByEvaluationForm_FormIDOrderByOrderIndexAsc(reviewToUpdate.getEvaluationForm().getFormID())
                    .stream()
                    .map(EvaluationQuestion::getQuestionID)
                    .collect(Collectors.toSet());

            for (ReviewResponseDTO responseDTO : reviewDetailsDTO.getReviewResponses()) {
                if (!validQuestionIdsForForm.contains(responseDTO.getQuestionID())) {
                    throw new BadRequestException("Question ID " + responseDTO.getQuestionID() + " is not valid for Evaluation Form ID " + reviewToUpdate.getEvaluationForm().getFormID());
                }
                EvaluationQuestion question = questionRepository.findById(responseDTO.getQuestionID())
                        .orElseThrow(() -> new BadRequestException("Question not found with ID: " + responseDTO.getQuestionID()));

                ReviewResponse response = ReviewResponse.builder()
                        .responseID(responseDTO.getResponseID()) // Will be null for new responses within update
                        .evaluationQuestion(question)
                        .responseText(responseDTO.getResponseText())
                        .numericResponse(responseDTO.getNumericResponse())
                        .performanceReview(reviewToUpdate)
                        .build();
                reviewToUpdate.addReviewResponse(response);
            }
        }

        PerformanceReview updatedReview = reviewRepository.save(reviewToUpdate);
        return convertToResponseDto(updatedReview);
    }

    @Override
    public void deletePerformanceReview(Integer id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Performance review not found with ID: " + id);
        }
        reviewRepository.deleteById(id);
    }

    @Override
    public List<PerformanceReviewResponseDTO> getReviewsByEmployee(Integer employeeId) {
        return reviewRepository.findByEmployee_EmployeeID(employeeId).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PerformanceReviewResponseDTO> getReviewsByEvaluator(Integer evaluatorId) {
        return reviewRepository.findByEvaluator_EmployeeID(evaluatorId).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
}