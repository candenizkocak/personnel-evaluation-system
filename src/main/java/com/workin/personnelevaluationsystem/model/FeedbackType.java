// src/main/java/com/workin/personnelevaluationsystem/model/FeedbackType.java
package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "FeedbackTypes") // Maps to the FeedbackTypes table in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FeedbackTypeID")
    private Integer feedbackTypeID;

    @Column(name = "Name", nullable = false, length = 50)
    private String name;

    @Column(name = "Description", length = 200)
    private String description;
}