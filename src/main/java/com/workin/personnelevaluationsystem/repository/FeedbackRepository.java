package com.workin.personnelevaluationsystem.repository;

import com.workin.personnelevaluationsystem.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback> findBySender_EmployeeID(Integer senderId);
    List<Feedback> findByReceiver_EmployeeID(Integer receiverId);
    List<Feedback> findByFeedbackType_FeedbackTypeID(Integer typeId);
}