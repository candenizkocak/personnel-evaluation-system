package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.EmployeeCreateDTO; // Use for input
import com.workin.personnelevaluationsystem.dto.EmployeeResponseDTO; // Use for output

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    EmployeeResponseDTO createEmployee(EmployeeCreateDTO employeeCreateDTO); // Accepts CreateDTO, returns ResponseDTO
    Optional<EmployeeResponseDTO> getEmployeeById(Integer id); // Returns ResponseDTO
    List<EmployeeResponseDTO> getAllEmployees(); // Returns list of ResponseDTOs
    EmployeeResponseDTO updateEmployee(Integer id, EmployeeCreateDTO employeeDetailsDTO); // Accepts CreateDTO, returns ResponseDTO
    void deleteEmployee(Integer id);
}