package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.FeedbackTypeDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.FeedbackType;
import com.workin.personnelevaluationsystem.repository.FeedbackTypeRepository;
import com.workin.personnelevaluationsystem.service.FeedbackTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedbackTypeServiceImpl implements FeedbackTypeService {

    private final FeedbackTypeRepository feedbackTypeRepository;

    @Autowired
    public FeedbackTypeServiceImpl(FeedbackTypeRepository feedbackTypeRepository) {
        this.feedbackTypeRepository = feedbackTypeRepository;
    }

    private FeedbackTypeDTO convertToDto(FeedbackType feedbackType) {
        if (feedbackType == null) return null;
        return FeedbackTypeDTO.builder()
                .feedbackTypeID(feedbackType.getFeedbackTypeID())
                .name(feedbackType.getName())
                .description(feedbackType.getDescription())
                .build();
    }

    private FeedbackType convertToEntity(FeedbackTypeDTO feedbackTypeDTO) {
        if (feedbackTypeDTO == null) return null;
        return FeedbackType.builder()
                .feedbackTypeID(feedbackTypeDTO.getFeedbackTypeID())
                .name(feedbackTypeDTO.getName())
                .description(feedbackTypeDTO.getDescription())
                .build();
    }

    @Override
    public FeedbackTypeDTO createFeedbackType(FeedbackTypeDTO typeDTO) {
        if (feedbackTypeRepository.findByName(typeDTO.getName()).isPresent()) {
            throw new BadRequestException("Feedback type with name '" + typeDTO.getName() + "' already exists.");
        }
        FeedbackType feedbackType = convertToEntity(typeDTO);
        FeedbackType savedFeedbackType = feedbackTypeRepository.save(feedbackType);
        return convertToDto(savedFeedbackType);
    }

    @Override
    public Optional<FeedbackTypeDTO> getFeedbackTypeById(Integer id) {
        return feedbackTypeRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<FeedbackTypeDTO> getAllFeedbackTypes() {
        return feedbackTypeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public FeedbackTypeDTO updateFeedbackType(Integer id, FeedbackTypeDTO typeDetailsDTO) {
        FeedbackType feedbackTypeToUpdate = feedbackTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback type not found with ID: " + id));

        if (!feedbackTypeToUpdate.getName().equals(typeDetailsDTO.getName())) {
            feedbackTypeRepository.findByName(typeDetailsDTO.getName())
                    .ifPresent(ft -> {
                        if (!ft.getFeedbackTypeID().equals(id)) {
                            throw new BadRequestException("Feedback type with name '" + typeDetailsDTO.getName() + "' already exists.");
                        }
                    });
        }

        feedbackTypeToUpdate.setName(typeDetailsDTO.getName());
        feedbackTypeToUpdate.setDescription(typeDetailsDTO.getDescription());

        FeedbackType updatedFeedbackType = feedbackTypeRepository.save(feedbackTypeToUpdate);
        return convertToDto(updatedFeedbackType);
    }

    @Override
    public void deleteFeedbackType(Integer id) {
        if (!feedbackTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Feedback type not found with ID: " + id);
        }
        // Add check for existing feedback using this type before deleting.
        // If the type is referenced by Feedback records, deleting it directly will cause a DataIntegrityViolationException.
        feedbackTypeRepository.deleteById(id);
    }
}