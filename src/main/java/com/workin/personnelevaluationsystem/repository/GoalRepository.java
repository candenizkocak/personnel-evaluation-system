package com.workin.personnelevaluationsystem.repository;

import com.workin.personnelevaluationsystem.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Integer> {
    List<Goal> findByEmployee_EmployeeID(Integer employeeId);
    List<Goal> findByGoalType_GoalTypeID(Integer goalTypeId);
    List<Goal> findByGoalStatus_StatusID(Integer statusId);
}