package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.FeedbackCreateDTO;
import com.workin.personnelevaluationsystem.dto.FeedbackResponseDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.Employee;
import com.workin.personnelevaluationsystem.model.Feedback;
import com.workin.personnelevaluationsystem.model.FeedbackType;
import com.workin.personnelevaluationsystem.repository.EmployeeRepository;
import com.workin.personnelevaluationsystem.repository.FeedbackRepository;
import com.workin.personnelevaluationsystem.repository.FeedbackTypeRepository;
import com.workin.personnelevaluationsystem.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final EmployeeRepository employeeRepository;
    private final FeedbackTypeRepository feedbackTypeRepository;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository,
                               EmployeeRepository employeeRepository,
                               FeedbackTypeRepository feedbackTypeRepository) {
        this.feedbackRepository = feedbackRepository;
        this.employeeRepository = employeeRepository;
        this.feedbackTypeRepository = feedbackTypeRepository;
    }

    private FeedbackResponseDTO convertToResponseDto(Feedback feedback) {
        if (feedback == null) return null;

        String senderFullName = feedback.getSender() != null ?
                feedback.getSender().getFirstName() + " " + feedback.getSender().getLastName() : null;
        // If feedback is anonymous, mask the sender's name
        if (feedback.getIsAnonymous() != null && feedback.getIsAnonymous()) {
            senderFullName = "Anonymous";
        }

        return FeedbackResponseDTO.builder()
                .feedbackID(feedback.getFeedbackID())
                .senderID(feedback.getSender() != null ? feedback.getSender().getEmployeeID() : null)
                .senderFullName(senderFullName)
                .receiverID(feedback.getReceiver() != null ? feedback.getReceiver().getEmployeeID() : null)
                .receiverFullName(feedback.getReceiver() != null ?
                        feedback.getReceiver().getFirstName() + " " + feedback.getReceiver().getLastName() : null)
                .feedbackTypeID(feedback.getFeedbackType() != null ? feedback.getFeedbackType().getFeedbackTypeID() : null)
                .feedbackTypeName(feedback.getFeedbackType() != null ? feedback.getFeedbackType().getName() : null)
                .content(feedback.getContent())
                .submissionDate(feedback.getSubmissionDate())
                .isAnonymous(feedback.getIsAnonymous())
                .build();
    }

    private Feedback convertToEntity(FeedbackCreateDTO feedbackDTO) {
        if (feedbackDTO == null) return null;

        Employee sender = employeeRepository.findById(feedbackDTO.getSenderID())
                .orElseThrow(() -> new BadRequestException("Sender employee not found with ID: " + feedbackDTO.getSenderID()));
        Employee receiver = employeeRepository.findById(feedbackDTO.getReceiverID())
                .orElseThrow(() -> new BadRequestException("Receiver employee not found with ID: " + feedbackDTO.getReceiverID()));
        FeedbackType feedbackType = feedbackTypeRepository.findById(feedbackDTO.getFeedbackTypeID())
                .orElseThrow(() -> new BadRequestException("Feedback Type not found with ID: " + feedbackDTO.getFeedbackTypeID()));

        // Business rule: Sender cannot be receiver
        if (sender.getEmployeeID().equals(receiver.getEmployeeID())) {
            throw new BadRequestException("An employee cannot send feedback to themselves.");
        }

        return Feedback.builder()
                .feedbackID(feedbackDTO.getFeedbackID())
                .sender(sender)
                .receiver(receiver)
                .feedbackType(feedbackType)
                .content(feedbackDTO.getContent())
                .submissionDate(feedbackDTO.getSubmissionDate() != null ? feedbackDTO.getSubmissionDate() : LocalDateTime.now())
                .isAnonymous(feedbackDTO.getIsAnonymous() != null ? feedbackDTO.getIsAnonymous() : false) // Default to false if not provided
                .build();
    }

    @Override
    @Transactional
    public FeedbackResponseDTO createFeedback(FeedbackCreateDTO feedbackDTO) {
        Feedback feedback = convertToEntity(feedbackDTO);
        Feedback savedFeedback = feedbackRepository.save(feedback);
        return convertToResponseDto(savedFeedback);
    }

    @Override
    public Optional<FeedbackResponseDTO> getFeedbackById(Integer id) {
        return feedbackRepository.findById(id).map(this::convertToResponseDto);
    }

    @Override
    public List<FeedbackResponseDTO> getAllFeedback() {
        return feedbackRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedbackResponseDTO> getFeedbackBySenderId(Integer senderId) {
        if (!employeeRepository.existsById(senderId)) {
            throw new ResourceNotFoundException("Sender employee not found with ID: " + senderId);
        }
        return feedbackRepository.findBySender_EmployeeID(senderId).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedbackResponseDTO> getFeedbackByReceiverId(Integer receiverId) {
        if (!employeeRepository.existsById(receiverId)) {
            throw new ResourceNotFoundException("Receiver employee not found with ID: " + receiverId);
        }
        return feedbackRepository.findByReceiver_EmployeeID(receiverId).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedbackResponseDTO> getFeedbackByTypeId(Integer feedbackTypeId) {
        if (!feedbackTypeRepository.existsById(feedbackTypeId)) {
            throw new ResourceNotFoundException("Feedback Type not found with ID: " + feedbackTypeId);
        }
        return feedbackRepository.findByFeedbackType_FeedbackTypeID(feedbackTypeId).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FeedbackResponseDTO updateFeedback(Integer id, FeedbackCreateDTO feedbackDetailsDTO) {
        Feedback feedbackToUpdate = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with ID: " + id));

        // Update relationships if provided
        if (feedbackDetailsDTO.getSenderID() != null) {
            Employee newSender = employeeRepository.findById(feedbackDetailsDTO.getSenderID())
                    .orElseThrow(() -> new BadRequestException("Sender employee not found with ID: " + feedbackDetailsDTO.getSenderID()));
            feedbackToUpdate.setSender(newSender);
        }
        if (feedbackDetailsDTO.getReceiverID() != null) {
            Employee newReceiver = employeeRepository.findById(feedbackDetailsDTO.getReceiverID())
                    .orElseThrow(() -> new BadRequestException("Receiver employee not found with ID: " + feedbackDetailsDTO.getReceiverID()));
            feedbackToUpdate.setReceiver(newReceiver);
        }
        if (feedbackDetailsDTO.getFeedbackTypeID() != null) {
            FeedbackType newFeedbackType = feedbackTypeRepository.findById(feedbackDetailsDTO.getFeedbackTypeID())
                    .orElseThrow(() -> new BadRequestException("Feedback Type not found with ID: " + feedbackDetailsDTO.getFeedbackTypeID()));
            feedbackToUpdate.setFeedbackType(newFeedbackType);
        }

        // Re-validate sender cannot be receiver after updates
        if (feedbackToUpdate.getSender() != null && feedbackToUpdate.getReceiver() != null &&
                feedbackToUpdate.getSender().getEmployeeID().equals(feedbackToUpdate.getReceiver().getEmployeeID())) {
            throw new BadRequestException("An employee cannot send feedback to themselves.");
        }

        // Update scalar fields, handling nulls to retain existing values if DTO field is null
        feedbackToUpdate.setContent(feedbackDetailsDTO.getContent() != null ? feedbackDetailsDTO.getContent() : feedbackToUpdate.getContent());
        feedbackToUpdate.setSubmissionDate(feedbackDetailsDTO.getSubmissionDate() != null ? feedbackDetailsDTO.getSubmissionDate() : feedbackToUpdate.getSubmissionDate());
        feedbackToUpdate.setIsAnonymous(feedbackDetailsDTO.getIsAnonymous() != null ? feedbackDetailsDTO.getIsAnonymous() : feedbackToUpdate.getIsAnonymous());

        Feedback updatedFeedback = feedbackRepository.save(feedbackToUpdate);
        return convertToResponseDto(updatedFeedback);
    }

    @Override
    public void deleteFeedback(Integer id) {
        if (!feedbackRepository.existsById(id)) {
            throw new ResourceNotFoundException("Feedback not found with ID: " + id);
        }
        feedbackRepository.deleteById(id);
    }
    @Override
    @Transactional(readOnly = true)
    public List<FeedbackResponseDTO> getTeamFeedback(Integer managerId) {
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + managerId));

        List<Employee> subordinates = employeeRepository.findByManager_EmployeeID(managerId);

        if (subordinates == null || subordinates.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> subordinateIds = subordinates.stream()
                .map(Employee::getEmployeeID)
                .collect(Collectors.toList());

        if (subordinateIds.isEmpty()){
            return new ArrayList<>();
        }

        // Fetch all feedback, then filter if either sender or receiver is a subordinate
        // This could be optimized with a more complex JPQL query if performance becomes an issue
        // for very large feedback tables.
        return feedbackRepository.findAll().stream()
                .filter(feedback ->
                        (feedback.getSender() != null && subordinateIds.contains(feedback.getSender().getEmployeeID())) ||
                                (feedback.getReceiver() != null && subordinateIds.contains(feedback.getReceiver().getEmployeeID()))
                )
                .map(this::convertToResponseDto)
                .sorted((f1, f2) -> f2.getSubmissionDate().compareTo(f1.getSubmissionDate())) // Sort by newest first
                .collect(Collectors.toList());
    }
}