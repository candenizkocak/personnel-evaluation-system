package com.workin.personnelevaluationsystem.repository;

import com.workin.personnelevaluationsystem.model.GoalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoalStatusRepository extends JpaRepository<GoalStatus, Integer> {
    Optional<GoalStatus> findByName(String name);
}