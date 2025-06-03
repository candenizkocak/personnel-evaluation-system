package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Entity
@Table(name = "EvaluationPeriods") // Maps to the EvaluationPeriods table in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PeriodID")
    private Integer periodID;

    @Column(name = "Name", nullable = false, length = 100)
    private String name;

    @Column(name = "StartDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "EndDate", nullable = false)
    private LocalDate endDate;

    @Column(name = "IsActive")
    private Boolean isActive;
}