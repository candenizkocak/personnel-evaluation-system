package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.EmployeeDTO;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    Optional<EmployeeDTO> getEmployeeById(Integer id);
    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO updateEmployee(Integer id, EmployeeDTO employeeDetailsDTO);
    void deleteEmployee(Integer id);
}