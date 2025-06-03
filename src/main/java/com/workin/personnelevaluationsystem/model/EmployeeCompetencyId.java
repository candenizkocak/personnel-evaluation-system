package com.workin.personnelevaluationsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

// This class represents the composite primary key for EmployeeCompetency
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCompetencyId implements Serializable {
    private Integer employee; // Corresponds to the 'employee' field in EmployeeCompetency entity
    private Integer competency; // Corresponds to the 'competency' field in EmployeeCompetency entity
    private LocalDate assessmentDate; // Corresponds to the 'assessmentDate' field in EmployeeCompetency entity

    // IMPORTANT: Equals and hashCode methods are crucial for composite keys.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeCompetencyId that = (EmployeeCompetencyId) o;
        return Objects.equals(employee, that.employee) &&
                Objects.equals(competency, that.competency) &&
                Objects.equals(assessmentDate, that.assessmentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, competency, assessmentDate);
    }
}