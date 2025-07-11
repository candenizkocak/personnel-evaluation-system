package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.PerformanceReviewCreateDTO;
import com.workin.personnelevaluationsystem.dto.PerformanceReviewResponseDTO;
import com.workin.personnelevaluationsystem.dto.ReviewResponseDTO;
import com.workin.personnelevaluationsystem.dto.ReviewSubmissionDTO;
import com.workin.personnelevaluationsystem.dto.EmployeeAverageScoreDTO; // Import new DTO
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.*;
import com.workin.personnelevaluationsystem.repository.*;
import com.workin.personnelevaluationsystem.service.PerformanceReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager; // For custom query if needed

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PerformanceReviewServiceImpl implements PerformanceReviewService {

    private final PerformanceReviewRepository reviewRepository;
    private final EmployeeRepository employeeRepository;
    private final EvaluationPeriodRepository periodRepository;
    private final EvaluationFormRepository formRepository;
    private final ReviewResponseRepository reviewResponseRepository;
    private final EntityManager entityManager; // Inject EntityManager

    @Autowired
    public PerformanceReviewServiceImpl(PerformanceReviewRepository reviewRepository,
                                        EmployeeRepository employeeRepository,
                                        EvaluationPeriodRepository periodRepository,
                                        EvaluationFormRepository formRepository,
                                        ReviewResponseRepository reviewResponseRepository,
                                        EntityManager entityManager) { // Add EntityManager to constructor
        this.reviewRepository = reviewRepository;
        this.employeeRepository = employeeRepository;
        this.periodRepository = periodRepository;
        this.formRepository = formRepository;
        this.reviewResponseRepository = reviewResponseRepository;
        this.entityManager = entityManager;
    }

    private ReviewResponseDTO convertResponseToDto(ReviewResponse response) {
        if (response == null) return null;
        EvaluationQuestion question = response.getEvaluationQuestion();
        return ReviewResponseDTO.builder()
                .responseID(response.getResponseID())
                .questionID(question != null ? question.getQuestionID() : null)
                .questionText(question != null ? question.getQuestionText() : "N/A")
                // ADD THIS LINE to populate questionTypeID
                .questionTypeID(question != null && question.getQuestionType() != null ? question.getQuestionType().getQuestionTypeID() : null)
                .responseText(response.getResponseText())
                .numericResponse(response.getNumericResponse())
                .build();
    }

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

    @Override
    @Transactional
    public PerformanceReviewResponseDTO createPerformanceReview(PerformanceReviewCreateDTO reviewDTO) {
        Employee employee = employeeRepository.findById(reviewDTO.getEmployeeID())
                .orElseThrow(() -> new BadRequestException("Employee being reviewed not found with ID: " + reviewDTO.getEmployeeID()));
        Employee evaluator = employeeRepository.findById(reviewDTO.getEvaluatorID())
                .orElseThrow(() -> new BadRequestException("Evaluator not found with ID: " + reviewDTO.getEvaluatorID()));
        EvaluationPeriod period = periodRepository.findById(reviewDTO.getPeriodID())
                .orElseThrow(() -> new BadRequestException("Evaluation Period not found with ID: " + reviewDTO.getPeriodID()));
        EvaluationForm form = formRepository.findById(reviewDTO.getFormID())
                .orElseThrow(() -> new BadRequestException("Evaluation Form not found with ID: " + reviewDTO.getFormID()));

        PerformanceReview review = PerformanceReview.builder()
                .employee(employee)
                .evaluator(evaluator)
                .evaluationPeriod(period)
                .evaluationForm(form)
                .status("Draft")
                .build();

        List<ReviewResponse> initialResponses = form.getQuestions().stream()
                .map(question -> ReviewResponse.builder()
                        .performanceReview(review)
                        .evaluationQuestion(question)
                        .build())
                .collect(Collectors.toList());

        review.setReviewResponses(initialResponses);
        PerformanceReview savedReview = reviewRepository.save(review);
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
    public void saveReviewDraft(Integer reviewId, ReviewSubmissionDTO submissionDTO) {
        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + reviewId));
        updateResponsesFromDTO(review, submissionDTO);
    }

    @Override
    @Transactional
    public PerformanceReviewResponseDTO submitFinalReview(Integer reviewId, ReviewSubmissionDTO submissionDTO) {
        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + reviewId));
        updateResponsesFromDTO(review, submissionDTO);
        BigDecimal finalScore = calculateFinalScore(review);
        review.setFinalScore(finalScore);
        review.setStatus("Submitted");
        review.setSubmissionDate(LocalDateTime.now());
        PerformanceReview finalReview = reviewRepository.save(review);
        return convertToResponseDto(finalReview);
    }

    private void updateResponsesFromDTO(PerformanceReview review, ReviewSubmissionDTO submissionDTO) {
        Map<Integer, ReviewResponseDTO> submittedResponsesMap = submissionDTO.getReviewResponses().stream()
                .collect(Collectors.toMap(ReviewResponseDTO::getQuestionID, dto -> dto));

        for (ReviewResponse response : review.getReviewResponses()) {
            Integer questionId = response.getEvaluationQuestion().getQuestionID();
            ReviewResponseDTO submittedDto = submittedResponsesMap.get(questionId);
            if (submittedDto != null) {
                response.setResponseText(submittedDto.getResponseText());
                response.setNumericResponse(submittedDto.getNumericResponse());
            }
        }
        reviewResponseRepository.saveAll(review.getReviewResponses());
    }

    private BigDecimal calculateFinalScore(PerformanceReview review) {
        BigDecimal totalWeightedScore = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;

        for (ReviewResponse response : review.getReviewResponses()) {
            if (response.getNumericResponse() != null) {
                Integer weight = response.getEvaluationQuestion().getWeight();
                BigDecimal questionWeight = (weight != null && weight > 0) ? new BigDecimal(weight) : BigDecimal.ONE;
                totalWeightedScore = totalWeightedScore.add(response.getNumericResponse().multiply(questionWeight));
                totalWeight = totalWeight.add(questionWeight);
            }
        }

        if (totalWeight.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return totalWeightedScore.divide(totalWeight, 2, RoundingMode.HALF_UP);
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

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeAverageScoreDTO> getEmployeeAverageScores() {
        // This is the existing method for Admin/HR - fetch all
        List<PerformanceReview> submittedReviews = reviewRepository.findAll().stream()
                .filter(review -> review.getEmployee() != null && "Submitted".equalsIgnoreCase(review.getStatus()) && review.getFinalScore() != null)
                .collect(Collectors.toList());
        return calculateAveragesFromReviews(submittedReviews);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeAverageScoreDTO> getEmployeeAverageScoresForManager(Integer managerId) {
        // Employee manager = employeeRepository.findById(managerId) // No need to fetch manager entity if we only need subordinates
        //         .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + managerId));

        List<Employee> subordinates = employeeRepository.findByManager_EmployeeID(managerId); // Use repository method

        if (subordinates == null || subordinates.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> subordinateIds = subordinates.stream()
                .map(Employee::getEmployeeID)
                .collect(Collectors.toList());

        if (subordinateIds.isEmpty()) { // Should be redundant if subordinates list was checked, but safe
            return new ArrayList<>();
        }

        List<PerformanceReview> submittedReviewsForSubordinates = reviewRepository.findAll().stream()
                .filter(review -> review.getEmployee() != null &&
                                 subordinateIds.contains(review.getEmployee().getEmployeeID()) &&
                                 "Submitted".equalsIgnoreCase(review.getStatus()) &&
                                 review.getFinalScore() != null)
                .collect(Collectors.toList());

        return calculateAveragesFromReviews(submittedReviewsForSubordinates);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerformanceReviewResponseDTO> getReviewsForSubordinates(Integer managerId) {
        // Employee manager = employeeRepository.findById(managerId)
        //         .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + managerId));

        List<Employee> subordinates = employeeRepository.findByManager_EmployeeID(managerId); // Use repository method

        if (subordinates == null || subordinates.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> subordinateIds = subordinates.stream()
                .map(Employee::getEmployeeID)
                .collect(Collectors.toList());

        if (subordinateIds.isEmpty()) {
            return new ArrayList<>();
        }

        return reviewRepository.findAll().stream()
                .filter(review -> review.getEmployee() != null && subordinateIds.contains(review.getEmployee().getEmployeeID()))
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // Helper method to avoid code duplication
    private List<EmployeeAverageScoreDTO> calculateAveragesFromReviews(List<PerformanceReview> reviews) {
        if (reviews.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Employee, List<PerformanceReview>> reviewsByEmployee = reviews.stream()
                .filter(r -> r.getEmployee() != null) // Ensure employee is not null
                .collect(Collectors.groupingBy(PerformanceReview::getEmployee));

        List<EmployeeAverageScoreDTO> averageScores = new ArrayList<>();
        for (Map.Entry<Employee, List<PerformanceReview>> entry : reviewsByEmployee.entrySet()) {
            Employee employee = entry.getKey();
            List<PerformanceReview> employeeReviews = entry.getValue();

            if (employeeReviews.isEmpty()) continue;

            BigDecimal sumOfScores = employeeReviews.stream()
                    .map(PerformanceReview::getFinalScore)
                    .filter(score -> score != null) // Ensure score is not null before adding
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            long validReviewCount = employeeReviews.stream().filter(r -> r.getFinalScore() != null).count();
            if (validReviewCount == 0) continue;

            BigDecimal average = sumOfScores.divide(new BigDecimal(validReviewCount), 2, RoundingMode.HALF_UP);

            averageScores.add(new EmployeeAverageScoreDTO(
                    employee.getEmployeeID(),
                    employee.getFirstName() + " " + employee.getLastName(),
                    average,
                    (int) validReviewCount // Cast long to int
            ));
        }
        averageScores.sort((s1, s2) -> s2.getAverageScore().compareTo(s1.getAverageScore()));
        return averageScores;
    }
}

