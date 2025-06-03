package com.workin.personnelevaluationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.workin.personnelevaluationsystem.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // Example of a custom query method
    // Optional<Employee> findByEmail(String email);
    // List<Employee> findByPosition_PositionID(Integer positionId);
    // List<Employee> findByManager_EmployeeID(Integer managerId);
}