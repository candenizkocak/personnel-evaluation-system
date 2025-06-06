package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.EmployeeCreateDTO; // Import
import com.workin.personnelevaluationsystem.dto.EmployeeResponseDTO; // Import
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.Employee;
import com.workin.personnelevaluationsystem.model.Position;
import com.workin.personnelevaluationsystem.repository.EmployeeRepository;
import com.workin.personnelevaluationsystem.repository.PositionRepository;
import com.workin.personnelevaluationsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PositionRepository positionRepository) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
    }

    // Helper method to convert Entity to Response DTO
    private EmployeeResponseDTO convertToResponseDto(Employee employee) {
        if (employee == null) {
            return null;
        }
        return EmployeeResponseDTO.builder()
                .employeeID(employee.getEmployeeID())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .hireDate(employee.getHireDate())
                .isActive(employee.getIsActive())
                // Populate enriched fields
                .positionID(employee.getPosition() != null ? employee.getPosition().getPositionID() : null)
                .positionTitle(employee.getPosition() != null ? employee.getPosition().getTitle() : null)
                .managerID(employee.getManager() != null ? employee.getManager().getEmployeeID() : null)
                .managerFullName(employee.getManager() != null ?
                        employee.getManager().getFirstName() + " " + employee.getManager().getLastName() : null)
                .build();
    }

    // Helper method to convert Create DTO to Entity
    private Employee convertToEntity(EmployeeCreateDTO employeeCreateDTO) {
        if (employeeCreateDTO == null) {
            return null;
        }
        Employee employee = Employee.builder()
                .employeeID(employeeCreateDTO.getEmployeeID()) // ID might be null for creation
                .firstName(employeeCreateDTO.getFirstName())
                .lastName(employeeCreateDTO.getLastName())
                .email(employeeCreateDTO.getEmail())
                .phone(employeeCreateDTO.getPhone())
                .hireDate(employeeCreateDTO.getHireDate())
                .isActive(employeeCreateDTO.getIsActive())
                .build();

        // Handle potential duplicate email for new employees or changed emails
        if (employeeCreateDTO.getEmail() != null) {
            employeeRepository.findByEmail(employeeCreateDTO.getEmail()).ifPresent(existingEmployee -> {
                if (employeeCreateDTO.getEmployeeID() == null || !existingEmployee.getEmployeeID().equals(employeeCreateDTO.getEmployeeID())) {
                    throw new BadRequestException("Email '" + employeeCreateDTO.getEmail() + "' is already in use.");
                }
            });
        }


        // Set the Position relationship if positionID is provided
        if (employeeCreateDTO.getPositionID() != null) {
            Position position = positionRepository.findById(employeeCreateDTO.getPositionID())
                    .orElseThrow(() -> new BadRequestException("Position not found with ID: " + employeeCreateDTO.getPositionID()));
            employee.setPosition(position);
        } else {
            // If positionID is null, set position to null if allowed by schema (DepartmentID is NOT NULL in Position)
            // Based on your schema, PositionID in Employees IS nullable, so this is fine.
            employee.setPosition(null);
        }

        // Set the Manager relationship if managerID is provided
        if (employeeCreateDTO.getManagerID() != null) {
            Employee manager = employeeRepository.findById(employeeCreateDTO.getManagerID())
                    .orElseThrow(() -> new BadRequestException("Manager not found with ID: " + employeeCreateDTO.getManagerID()));
            // Prevent an employee from being their own manager during creation/update
            if (employeeCreateDTO.getEmployeeID() != null && employeeCreateDTO.getEmployeeID().equals(employeeCreateDTO.getManagerID())) {
                throw new BadRequestException("An employee cannot be their own manager.");
            }
            employee.setManager(manager);
        } else {
            employee.setManager(null); // Allow employee without manager
        }
        return employee;
    }

    @Override
    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeCreateDTO employeeCreateDTO) {
        Employee employee = convertToEntity(employeeCreateDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToResponseDto(savedEmployee);
    }

    @Override
    public Optional<EmployeeResponseDTO> getEmployeeById(Integer id) {
        return employeeRepository.findById(id)
                .map(this::convertToResponseDto);
    }

    @Override
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeResponseDTO updateEmployee(Integer id, EmployeeCreateDTO employeeDetailsDTO) {
        Employee employeeToUpdate = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        // Update basic fields, handling nulls to retain existing values if DTO field is null
        employeeToUpdate.setFirstName(employeeDetailsDTO.getFirstName() != null ? employeeDetailsDTO.getFirstName() : employeeToUpdate.getFirstName());
        employeeToUpdate.setLastName(employeeDetailsDTO.getLastName() != null ? employeeDetailsDTO.getLastName() : employeeToUpdate.getLastName());
        employeeToUpdate.setEmail(employeeDetailsDTO.getEmail() != null ? employeeDetailsDTO.getEmail() : employeeToUpdate.getEmail());
        employeeToUpdate.setPhone(employeeDetailsDTO.getPhone() != null ? employeeDetailsDTO.getPhone() : employeeToUpdate.getPhone());
        employeeToUpdate.setHireDate(employeeDetailsDTO.getHireDate() != null ? employeeDetailsDTO.getHireDate() : employeeToUpdate.getHireDate());
        employeeToUpdate.setIsActive(employeeDetailsDTO.getIsActive() != null ? employeeDetailsDTO.getIsActive() : employeeToUpdate.getIsActive());

        // Handle potential duplicate email for existing employees
        if (employeeDetailsDTO.getEmail() != null && !employeeToUpdate.getEmail().equals(employeeDetailsDTO.getEmail())) {
            employeeRepository.findByEmail(employeeDetailsDTO.getEmail()).ifPresent(existingEmployee -> {
                if (!existingEmployee.getEmployeeID().equals(id)) { // If existing email belongs to a different employee
                    throw new BadRequestException("Email '" + employeeDetailsDTO.getEmail() + "' is already in use by another employee.");
                }
            });
        }


        // Update Position relationship
        if (employeeDetailsDTO.getPositionID() != null) {
            Position newPosition = positionRepository.findById(employeeDetailsDTO.getPositionID())
                    .orElseThrow(() -> new BadRequestException("Position not found with ID: " + employeeDetailsDTO.getPositionID()));
            employeeToUpdate.setPosition(newPosition);
        } else {
            employeeToUpdate.setPosition(null); // Clear association if ID is null
        }

        // Update Manager relationship
        if (employeeDetailsDTO.getManagerID() != null) {
            Employee newManager = employeeRepository.findById(employeeDetailsDTO.getManagerID())
                    .orElseThrow(() -> new BadRequestException("Manager not found with ID: " + employeeDetailsDTO.getManagerID()));
            // Prevent an employee from being their own manager
            if (newManager.getEmployeeID().equals(id)) {
                throw new BadRequestException("An employee cannot be their own manager.");
            }
            employeeToUpdate.setManager(newManager);
        } else {
            employeeToUpdate.setManager(null); // Clear association if ID is null
        }

        Employee updatedEmployee = employeeRepository.save(employeeToUpdate);
        return convertToResponseDto(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Integer id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with ID: " + id);
        }
        // IMPORTANT: Consider cascading deletes or checks here.
        // An employee might be a manager, have reviews, goals, feedback, notifications.
        // Direct delete will likely cause DataIntegrityViolationException.
        // For a robust system, you'd implement logic to:
        // 1. Set their 'IsActive' to false instead of deleting.
        // 2. Disassociate them from roles, managers, reviews, goals, etc.
        // For now, it will throw an exception if referenced.
        employeeRepository.deleteById(id);
    }
}