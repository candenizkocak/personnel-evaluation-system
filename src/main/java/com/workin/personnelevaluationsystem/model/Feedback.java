// src/main/java/com/workin/personnelevaluationsystem/model/Feedback.java
package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "Feedback") // Maps to the Feedback table in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FeedbackID")
    private Integer feedbackID;

    // Many-to-One relationship with Employee (sender)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SenderID") // Foreign key column in Feedback table
    private Employee sender;

    // Many-to-One relationship with Employee (receiver)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReceiverID") // Foreign key column in Feedback table
    private Employee receiver;

    // Many-to-One relationship with FeedbackType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FeedbackTypeID") // Foreign key column in Feedback table
    private FeedbackType feedbackType;

    @Column(name = "Content", nullable = false, columnDefinition = "NVARCHAR(MAX)") // NVARCHAR(MAX) in SQL
    private String content;

    @Column(name = "SubmissionDate") // DATETIME DEFAULT GETDATE() in SQL
    private LocalDateTime submissionDate;

    @Column(name = "IsAnonymous") // BIT DEFAULT 0 in SQL
    private Boolean isAnonymous;
}