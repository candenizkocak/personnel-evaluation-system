package com.workin.personnelevaluationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.workin.personnelevaluationsystem.model.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {
    // Example of a custom query method (not required by the SQL script, but good to illustrate)
    // Spring Data JPA can derive the query from the method name.
    // List<Position> findByDepartment_DepartmentID(Integer departmentId);
}