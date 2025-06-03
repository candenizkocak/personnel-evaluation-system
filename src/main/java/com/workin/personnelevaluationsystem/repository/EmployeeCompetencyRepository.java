package com.workin.personnelevaluationsystem.repository;

import com.workin.personnelevaluationsystem.model.EmployeeCompetency;
import com.workin.personnelevaluationsystem.model.EmployeeCompetencyId; // Import the composite key
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeCompetencyRepository extends JpaRepository<EmployeeCompetency, EmployeeCompetencyId> {
    List<EmployeeCompetency> findByEmployee_EmployeeID(Integer employeeId);
    List<EmployeeCompetency> findByCompetency_CompetencyID(Integer competencyId);
    List<EmployeeCompetency> findByEmployee_EmployeeIDAndCompetency_CompetencyIDOrderByAssessmentDateDesc(Integer employeeId, Integer competencyId);
}