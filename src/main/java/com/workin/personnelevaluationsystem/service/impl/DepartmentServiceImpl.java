package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.DepartmentDTO;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException; // Import new exception
import com.workin.personnelevaluationsystem.model.Department;
import com.workin.personnelevaluationsystem.repository.DepartmentRepository;
import com.workin.personnelevaluationsystem.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    private DepartmentDTO convertToDto(Department department) {
        if (department == null) return null;
        return DepartmentDTO.builder()
                .departmentID(department.getDepartmentID())
                .name(department.getName())
                .description(department.getDescription())
                .isActive(department.getIsActive())
                .build();
    }

    private Department convertToEntity(DepartmentDTO departmentDTO) {
        if (departmentDTO == null) return null;
        return Department.builder()
                .departmentID(departmentDTO.getDepartmentID())
                .name(departmentDTO.getName())
                .description(departmentDTO.getDescription())
                .isActive(departmentDTO.getIsActive())
                .build();
    }

    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        Department department = convertToEntity(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);
        return convertToDto(savedDepartment);
    }

    @Override
    public Optional<DepartmentDTO> getDepartmentById(Integer id) {
        // No change here for Optional return, controller will handle .orElseGet
        return departmentRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO updateDepartment(Integer id, DepartmentDTO departmentDetailsDTO) {
        Department departmentToUpdate = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));

        departmentToUpdate.setName(departmentDetailsDTO.getName());
        departmentToUpdate.setDescription(departmentDetailsDTO.getDescription());
        departmentToUpdate.setIsActive(departmentDetailsDTO.getIsActive());

        Department updatedDepartment = departmentRepository.save(departmentToUpdate);
        return convertToDto(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Integer id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with ID: " + id);
        }
        departmentRepository.deleteById(id);
    }
}