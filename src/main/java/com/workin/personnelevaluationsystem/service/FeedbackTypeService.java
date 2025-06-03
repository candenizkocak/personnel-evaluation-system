package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.FeedbackTypeDTO;

import java.util.List;
import java.util.Optional;

public interface FeedbackTypeService {
    FeedbackTypeDTO createFeedbackType(FeedbackTypeDTO typeDTO);
    Optional<FeedbackTypeDTO> getFeedbackTypeById(Integer id);
    List<FeedbackTypeDTO> getAllFeedbackTypes();
    FeedbackTypeDTO updateFeedbackType(Integer id, FeedbackTypeDTO typeDetailsDTO);
    void deleteFeedbackType(Integer id);
}