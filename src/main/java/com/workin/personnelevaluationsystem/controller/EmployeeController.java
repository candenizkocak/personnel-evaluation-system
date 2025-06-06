package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.EmployeeCreateDTO; // Import
import com.workin.personnelevaluationsystem.dto.EmployeeResponseDTO; // Import
import com.workin.personnelevaluationsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeCreateDTO employeeDTO) { // Accepts CreateDTO
        EmployeeResponseDTO createdEmployee = employeeService.createEmployee(employeeDTO); // Returns ResponseDTO
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Integer id) { // Returns ResponseDTO
        return employeeService.getEmployeeById(id)
                .map(employeeDTO -> new ResponseEntity<>(employeeDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() { // Returns List of ResponseDTOs
        List<EmployeeResponseDTO> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Integer id, @Valid @RequestBody EmployeeCreateDTO employeeDetailsDTO) { // Accepts CreateDTO
        EmployeeResponseDTO updatedEmployee = employeeService.updateEmployee(id, employeeDetailsDTO); // Returns ResponseDTO
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}