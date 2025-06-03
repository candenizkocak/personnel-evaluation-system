package com.workin.personnelevaluationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.workin.personnelevaluationsystem.model.Department;

// @Repository is a Spring stereotype annotation that indicates that an annotated class is a "Repository".
// JpaRepository provides methods for CRUD operations (Create, Read, Update, Delete) and
// is specialized for JPA. It takes two generic parameters:
// 1. The Entity type (Department)
// 2. The type of the Entity's primary key (Integer for DepartmentID)
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    // Spring Data JPA automatically provides common methods like save(), findById(), findAll(), deleteById(), etc.
    // You can add custom query methods here if needed, following Spring Data JPA's naming conventions,
    // e.g., findByName(String name), findByIsActive(Boolean isActive), etc.
}