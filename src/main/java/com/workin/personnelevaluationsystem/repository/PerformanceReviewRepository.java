package com.workin.personnelevaluationsystem.repository;

import com.workin.personnelevaluationsystem.model.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Integer> {
    List<PerformanceReview> findByEmployee_EmployeeID(Integer employeeId);
    List<PerformanceReview> findByEvaluator_EmployeeID(Integer evaluatorId);
    List<PerformanceReview> findByEvaluationPeriod_PeriodID(Integer periodId);
    List<PerformanceReview> findByEvaluationForm_FormID(Integer formId);
}