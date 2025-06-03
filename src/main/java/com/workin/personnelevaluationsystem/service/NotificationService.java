package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.NotificationCreateDTO;
import com.workin.personnelevaluationsystem.dto.NotificationResponseDTO;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    NotificationResponseDTO createNotification(NotificationCreateDTO notificationDTO);
    Optional<NotificationResponseDTO> getNotificationById(Integer id);
    List<NotificationResponseDTO> getAllNotifications();
    List<NotificationResponseDTO> getNotificationsByEmployeeId(Integer employeeId);
    List<NotificationResponseDTO> getUnreadNotificationsByEmployeeId(Integer employeeId);
    NotificationResponseDTO updateNotification(Integer id, NotificationCreateDTO notificationDetailsDTO);
    void deleteNotification(Integer id);
    NotificationResponseDTO markNotificationAsRead(Integer id); // Specific business operation
}