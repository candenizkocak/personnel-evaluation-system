package com.workin.personnelevaluationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.workin.personnelevaluationsystem.model.Employee;

import java.util.List; // Import List
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByEmail(String email);
    List<Employee> findByManager_EmployeeID(Integer managerId);
}