package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.FeedbackCreateDTO;
import com.workin.personnelevaluationsystem.dto.FeedbackResponseDTO;

import java.util.List;
import java.util.Optional;

public interface FeedbackService {
    FeedbackResponseDTO createFeedback(FeedbackCreateDTO feedbackDTO);
    Optional<FeedbackResponseDTO> getFeedbackById(Integer id);
    List<FeedbackResponseDTO> getAllFeedback();
    List<FeedbackResponseDTO> getFeedbackBySenderId(Integer senderId);
    List<FeedbackResponseDTO> getFeedbackByReceiverId(Integer receiverId);
    List<FeedbackResponseDTO> getFeedbackByTypeId(Integer feedbackTypeId);
    FeedbackResponseDTO updateFeedback(Integer id, FeedbackCreateDTO feedbackDetailsDTO);
    void deleteFeedback(Integer id);
}