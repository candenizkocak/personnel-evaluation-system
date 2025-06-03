package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.EmployeeDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException; // Import new exception
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException; // Import new exception
import com.workin.personnelevaluationsystem.model.Employee;
import com.workin.personnelevaluationsystem.model.Position;
import com.workin.personnelevaluationsystem.repository.EmployeeRepository;
import com.workin.personnelevaluationsystem.repository.PositionRepository;
import com.workin.personnelevaluationsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private EmployeeDTO convertToDto(Employee employee) {
        if (employee == null) return null;
        return EmployeeDTO.builder()
                .employeeID(employee.getEmployeeID())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .hireDate(employee.getHireDate())
                .positionID(employee.getPosition() != null ? employee.getPosition().getPositionID() : null)
                .managerID(employee.getManager() != null ? employee.getManager().getEmployeeID() : null)
                .isActive(employee.getIsActive())
                .build();
    }

    private Employee convertToEntity(EmployeeDTO employeeDTO) {
        if (employeeDTO == null) return null;
        Employee employee = Employee.builder()
                .employeeID(employeeDTO.getEmployeeID())
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .email(employeeDTO.getEmail())
                .phone(employeeDTO.getPhone())
                .hireDate(employeeDTO.getHireDate())
                .isActive(employeeDTO.getIsActive())
                .build();

        if (employeeDTO.getPositionID() != null) {
            Position position = positionRepository.findById(employeeDTO.getPositionID())
                    .orElseThrow(() -> new BadRequestException("Position not found with ID: " + employeeDTO.getPositionID() + " for employee creation/update."));
            employee.setPosition(position);
        }

        if (employeeDTO.getManagerID() != null) {
            // Ensure manager exists
            Employee manager = employeeRepository.findById(employeeDTO.getManagerID())
                    .orElseThrow(() -> new BadRequestException("Manager not found with ID: " + employeeDTO.getManagerID() + " for employee creation/update."));
            // Prevent an employee from being their own manager during creation
            if (employeeDTO.getEmployeeID() != null && employeeDTO.getEmployeeID().equals(employeeDTO.getManagerID())) {
                throw new BadRequestException("An employee cannot be their own manager.");
            }
            employee.setManager(manager);
        }
        return employee;
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = convertToEntity(employeeDTO); // This might throw BadRequestException
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDto(savedEmployee);
    }

    @Override
    public Optional<EmployeeDTO> getEmployeeById(Integer id) {
        return employeeRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO updateEmployee(Integer id, EmployeeDTO employeeDetailsDTO) {
        Employee employeeToUpdate = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        employeeToUpdate.setFirstName(employeeDetailsDTO.getFirstName());
        employeeToUpdate.setLastName(employeeDetailsDTO.getLastName());
        employeeToUpdate.setEmail(employeeDetailsDTO.getEmail());
        employeeToUpdate.setPhone(employeeDetailsDTO.getPhone());
        employeeToUpdate.setHireDate(employeeDetailsDTO.getHireDate());
        employeeToUpdate.setIsActive(employeeDetailsDTO.getIsActive());

        if (employeeDetailsDTO.getPositionID() != null) {
            Position newPosition = positionRepository.findById(employeeDetailsDTO.getPositionID())
                    .orElseThrow(() -> new BadRequestException("Position not found with ID: " + employeeDetailsDTO.getPositionID() + " for employee update."));
            employeeToUpdate.setPosition(newPosition);
        } else {
            employeeToUpdate.setPosition(null);
        }

        if (employeeDetailsDTO.getManagerID() != null) {
            Employee newManager = employeeRepository.findById(employeeDetailsDTO.getManagerID())
                    .orElseThrow(() -> new BadRequestException("Manager not found with ID: " + employeeDetailsDTO.getManagerID() + " for employee update."));
            // Prevent an employee from being their own manager
            if (newManager.getEmployeeID().equals(id)) {
                throw new BadRequestException("An employee cannot be their own manager.");
            }
            employeeToUpdate.setManager(newManager);
        } else {
            employeeToUpdate.setManager(null);
        }

        Employee updatedEmployee = employeeRepository.save(employeeToUpdate);
        return convertToDto(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Integer id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with ID: " + id);
        }
        employeeRepository.deleteById(id);
    }
}