package com.workin.personnelevaluationsystem.repository;

import com.workin.personnelevaluationsystem.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByEmployee_EmployeeID(Integer employeeId);
    List<Notification> findByEmployee_EmployeeIDAndIsRead(Integer employeeId, Boolean isRead);
    List<Notification> findByRelatedEntityTypeAndRelatedEntityID(String entityType, Integer entityId);
}