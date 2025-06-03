package com.workin.personnelevaluationsystem.repository;

import com.workin.personnelevaluationsystem.model.FeedbackType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackTypeRepository extends JpaRepository<FeedbackType, Integer> {
    Optional<FeedbackType> findByName(String name);
}