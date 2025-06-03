package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.DepartmentDTO; // Import the DTO class
import com.workin.personnelevaluationsystem.model.Department; // Keep import for internal use if needed

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    // Change return type and parameter type to DepartmentDTO
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);
    Optional<DepartmentDTO> getDepartmentById(Integer id); // Returns DTO
    List<DepartmentDTO> getAllDepartments(); // Returns list of DTOs
    DepartmentDTO updateDepartment(Integer id, DepartmentDTO departmentDetailsDTO); // Parameters are DTOs
    void deleteDepartment(Integer id);
}