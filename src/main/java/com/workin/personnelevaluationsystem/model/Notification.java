package com.workin.personnelevaluationsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "Notifications") // Maps to the Notifications table in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NotificationID")
    private Integer notificationID;

    // Many-to-One relationship with Employee (the recipient of the notification)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeID") // Foreign key column in Notifications table
    private Employee employee;

    @Column(name = "Message", nullable = false, length = 500)
    private String message;

    @Column(name = "Created") // DATETIME DEFAULT GETDATE() in SQL
    private LocalDateTime created;

    @Column(name = "IsRead") // BIT DEFAULT 0 in SQL
    private Boolean isRead;

    @Column(name = "RelatedEntityType", length = 50)
    private String relatedEntityType; // e.g., "PerformanceReview", "Goal"

    @Column(name = "RelatedEntityID")
    private Integer relatedEntityID;
}