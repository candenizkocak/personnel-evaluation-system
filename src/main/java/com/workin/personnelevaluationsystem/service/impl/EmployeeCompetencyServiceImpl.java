package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.EmployeeCompetencyDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.Competency;
import com.workin.personnelevaluationsystem.model.CompetencyLevel;
import com.workin.personnelevaluationsystem.model.Employee;
import com.workin.personnelevaluationsystem.model.EmployeeCompetency;
import com.workin.personnelevaluationsystem.model.EmployeeCompetencyId;
import com.workin.personnelevaluationsystem.repository.CompetencyLevelRepository;
import com.workin.personnelevaluationsystem.repository.CompetencyRepository;
import com.workin.personnelevaluationsystem.repository.EmployeeCompetencyRepository;
import com.workin.personnelevaluationsystem.repository.EmployeeRepository;
import com.workin.personnelevaluationsystem.service.EmployeeCompetencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeCompetencyServiceImpl implements EmployeeCompetencyService {

    private final EmployeeCompetencyRepository employeeCompetencyRepository;
    private final EmployeeRepository employeeRepository;
    private final CompetencyRepository competencyRepository;
    private final CompetencyLevelRepository competencyLevelRepository;

    @Autowired
    public EmployeeCompetencyServiceImpl(EmployeeCompetencyRepository employeeCompetencyRepository,
                                         EmployeeRepository employeeRepository,
                                         CompetencyRepository competencyRepository,
                                         CompetencyLevelRepository competencyLevelRepository) {
        this.employeeCompetencyRepository = employeeCompetencyRepository;
        this.employeeRepository = employeeRepository;
        this.competencyRepository = competencyRepository;
        this.competencyLevelRepository = competencyLevelRepository;
    }

    private EmployeeCompetencyDTO convertToDto(EmployeeCompetency employeeCompetency) {
        if (employeeCompetency == null) return null;
        return EmployeeCompetencyDTO.builder()
                .employeeID(employeeCompetency.getEmployee() != null ? employeeCompetency.getEmployee().getEmployeeID() : null)
                .employeeName(employeeCompetency.getEmployee() != null ?
                        employeeCompetency.getEmployee().getFirstName() + " " + employeeCompetency.getEmployee().getLastName() : null)
                .competencyID(employeeCompetency.getCompetency() != null ? employeeCompetency.getCompetency().getCompetencyID() : null)
                .competencyName(employeeCompetency.getCompetency() != null ? employeeCompetency.getCompetency().getName() : null)
                .assessmentDate(employeeCompetency.getAssessmentDate())
                .levelID(employeeCompetency.getLevel() != null ? employeeCompetency.getLevel().getLevelID() : null)
                .levelDescription(employeeCompetency.getLevel() != null ? employeeCompetency.getLevel().getDescription() : null)
                .assessedByID(employeeCompetency.getAssessedBy() != null ? employeeCompetency.getAssessedBy().getEmployeeID() : null)
                .assessedByName(employeeCompetency.getAssessedBy() != null ?
                        employeeCompetency.getAssessedBy().getFirstName() + " " + employeeCompetency.getAssessedBy().getLastName() : null)
                .build();
    }

    private EmployeeCompetency convertToEntity(EmployeeCompetencyDTO dto) {
        if (dto == null) return null;

        Employee employee = employeeRepository.findById(dto.getEmployeeID())
                .orElseThrow(() -> new BadRequestException("Employee not found with ID: " + dto.getEmployeeID()));
        Competency competency = competencyRepository.findById(dto.getCompetencyID())
                .orElseThrow(() -> new BadRequestException("Competency not found with ID: " + dto.getCompetencyID()));
        CompetencyLevel level = competencyLevelRepository.findById(dto.getLevelID())
                .orElseThrow(() -> new BadRequestException("Competency Level not found with ID: " + dto.getLevelID()));
        Employee assessedBy = employeeRepository.findById(dto.getAssessedByID())
                .orElseThrow(() -> new BadRequestException("Assessor employee not found with ID: " + dto.getAssessedByID()));

        // Optional: Validate that the chosen level belongs to the chosen competency
        if (!level.getCompetency().getCompetencyID().equals(competency.getCompetencyID())) {
            throw new BadRequestException("Competency level ID " + dto.getLevelID() + " does not belong to Competency ID " + dto.getCompetencyID());
        }

        return EmployeeCompetency.builder()
                .employee(employee)
                .competency(competency)
                .assessmentDate(dto.getAssessmentDate())
                .level(level)
                .assessedBy(assessedBy)
                .build();
    }

    @Override
    @Transactional
    public EmployeeCompetencyDTO createEmployeeCompetency(EmployeeCompetencyDTO employeeCompetencyDTO) {
        // Check if a record with the same composite key already exists
        EmployeeCompetencyId id = new EmployeeCompetencyId(
                employeeCompetencyDTO.getEmployeeID(),
                employeeCompetencyDTO.getCompetencyID(),
                employeeCompetencyDTO.getAssessmentDate()
        );
        if (employeeCompetencyRepository.existsById(id)) {
            throw new BadRequestException("Employee competency record already exists for Employee ID " +
                    employeeCompetencyDTO.getEmployeeID() + ", Competency ID " + employeeCompetencyDTO.getCompetencyID() +
                    " on " + employeeCompetencyDTO.getAssessmentDate());
        }

        EmployeeCompetency employeeCompetency = convertToEntity(employeeCompetencyDTO);
        EmployeeCompetency savedEmployeeCompetency = employeeCompetencyRepository.save(employeeCompetency);
        return convertToDto(savedEmployeeCompetency);
    }

    @Override
    public Optional<EmployeeCompetencyDTO> getEmployeeCompetencyById(Integer employeeId, Integer competencyId, LocalDate assessmentDate) {
        EmployeeCompetencyId id = new EmployeeCompetencyId(employeeId, competencyId, assessmentDate);
        return employeeCompetencyRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    public List<EmployeeCompetencyDTO> getAllEmployeeCompetencies() {
        return employeeCompetencyRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeCompetencyDTO> getEmployeeCompetenciesByEmployeeId(Integer employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee not found with ID: " + employeeId);
        }
        return employeeCompetencyRepository.findByEmployee_EmployeeID(employeeId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeCompetencyDTO> getEmployeeCompetenciesByCompetencyId(Integer competencyId) {
        if (!competencyRepository.existsById(competencyId)) {
            throw new ResourceNotFoundException("Competency not found with ID: " + competencyId);
        }
        return employeeCompetencyRepository.findByCompetency_CompetencyID(competencyId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeCompetencyDTO updateEmployeeCompetency(Integer employeeId, Integer competencyId, LocalDate assessmentDate, EmployeeCompetencyDTO employeeCompetencyDetailsDTO) {
        EmployeeCompetencyId idToUpdate = new EmployeeCompetencyId(employeeId, competencyId, assessmentDate);
        EmployeeCompetency employeeCompetencyToUpdate = employeeCompetencyRepository.findById(idToUpdate)
                .orElseThrow(() -> new ResourceNotFoundException("Employee competency record not found for Employee ID " + employeeId + ", Competency ID " + competencyId + " on " + assessmentDate));

        // Logic to prevent changing composite key fields if that's the rule
        if (!employeeId.equals(employeeCompetencyDetailsDTO.getEmployeeID()) ||
                !competencyId.equals(employeeCompetencyDetailsDTO.getCompetencyID()) ||
                !assessmentDate.equals(employeeCompetencyDetailsDTO.getAssessmentDate())) {
            throw new BadRequestException("Cannot change EmployeeID, CompetencyID, or AssessmentDate during update. These fields form the primary key.");
        }

        // Update associated entities
        if (employeeCompetencyDetailsDTO.getLevelID() != null) {
            CompetencyLevel newLevel = competencyLevelRepository.findById(employeeCompetencyDetailsDTO.getLevelID())
                    .orElseThrow(() -> new BadRequestException("Competency Level not found with ID: " + employeeCompetencyDetailsDTO.getLevelID()));
            // Validate that the new level belongs to the original competency
            if (!newLevel.getCompetency().getCompetencyID().equals(employeeCompetencyToUpdate.getCompetency().getCompetencyID())) {
                throw new BadRequestException("New competency level ID " + employeeCompetencyDetailsDTO.getLevelID() + " does not belong to the original Competency ID " + employeeCompetencyToUpdate.getCompetency().getCompetencyID());
            }
            employeeCompetencyToUpdate.setLevel(newLevel);
        }

        if (employeeCompetencyDetailsDTO.getAssessedByID() != null) {
            Employee newAssessedBy = employeeRepository.findById(employeeCompetencyDetailsDTO.getAssessedByID())
                    .orElseThrow(() -> new BadRequestException("Assessor employee not found with ID: " + employeeCompetencyDetailsDTO.getAssessedByID()));
            employeeCompetencyToUpdate.setAssessedBy(newAssessedBy);
        }

        EmployeeCompetency updatedEmployeeCompetency = employeeCompetencyRepository.save(employeeCompetencyToUpdate);
        return convertToDto(updatedEmployeeCompetency);
    }

    @Override
    public void deleteEmployeeCompetency(Integer employeeId, Integer competencyId, LocalDate assessmentDate) {
        EmployeeCompetencyId idToDelete = new EmployeeCompetencyId(employeeId, competencyId, assessmentDate);
        if (!employeeCompetencyRepository.existsById(idToDelete)) {
            throw new ResourceNotFoundException("Employee competency record not found for Employee ID " + employeeId + ", Competency ID " + competencyId + " on " + assessmentDate);
        }
        employeeCompetencyRepository.deleteById(idToDelete);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeCompetencyDTO> getCompetenciesForSubordinates(Integer managerId) {
        List<Employee> subordinates = employeeRepository.findByManager_EmployeeID(managerId); // Use repository method

        if (subordinates == null || subordinates.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> subordinateIds = subordinates.stream()
                .map(Employee::getEmployeeID)
                .collect(Collectors.toList());

        if (subordinateIds.isEmpty()) {
            return new ArrayList<>();
        }

        // Fetch all competencies and then filter.
        return employeeCompetencyRepository.findAll().stream()
                .filter(ec -> ec.getEmployee() != null && subordinateIds.contains(ec.getEmployee().getEmployeeID()))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}