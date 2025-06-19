package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.EmployeeCompetencyDTO;
import com.workin.personnelevaluationsystem.model.EmployeeCompetencyId; // Import the composite key

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeCompetencyService {
    EmployeeCompetencyDTO createEmployeeCompetency(EmployeeCompetencyDTO employeeCompetencyDTO);
    Optional<EmployeeCompetencyDTO> getEmployeeCompetencyById(Integer employeeId, Integer competencyId, LocalDate assessmentDate);
    List<EmployeeCompetencyDTO> getAllEmployeeCompetencies();
    List<EmployeeCompetencyDTO> getEmployeeCompetenciesByEmployeeId(Integer employeeId);
    List<EmployeeCompetencyDTO> getEmployeeCompetenciesByCompetencyId(Integer competencyId);
    EmployeeCompetencyDTO updateEmployeeCompetency(Integer employeeId, Integer competencyId, LocalDate assessmentDate, EmployeeCompetencyDTO employeeCompetencyDetailsDTO);
    void deleteEmployeeCompetency(Integer employeeId, Integer competencyId, LocalDate assessmentDate);
    List<EmployeeCompetencyDTO> getCompetenciesForSubordinates(Integer managerId);
}