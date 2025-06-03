package com.workin.personnelevaluationsystem.repository;

import com.workin.personnelevaluationsystem.model.GoalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoalTypeRepository extends JpaRepository<GoalType, Integer> {
    Optional<GoalType> findByName(String name);
}