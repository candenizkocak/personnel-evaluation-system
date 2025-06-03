package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.NotificationCreateDTO;
import com.workin.personnelevaluationsystem.dto.NotificationResponseDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.Employee;
import com.workin.personnelevaluationsystem.model.Notification;
import com.workin.personnelevaluationsystem.repository.EmployeeRepository;
import com.workin.personnelevaluationsystem.repository.NotificationRepository;
import com.workin.personnelevaluationsystem.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   EmployeeRepository employeeRepository) {
        this.notificationRepository = notificationRepository;
        this.employeeRepository = employeeRepository;
    }

    private NotificationResponseDTO convertToResponseDto(Notification notification) {
        if (notification == null) return null;
        return NotificationResponseDTO.builder()
                .notificationID(notification.getNotificationID())
                .employeeID(notification.getEmployee() != null ? notification.getEmployee().getEmployeeID() : null)
                .employeeFullName(notification.getEmployee() != null ?
                        notification.getEmployee().getFirstName() + " " + notification.getEmployee().getLastName() : null)
                .message(notification.getMessage())
                .created(notification.getCreated())
                .isRead(notification.getIsRead())
                .relatedEntityType(notification.getRelatedEntityType())
                .relatedEntityID(notification.getRelatedEntityID())
                .build();
    }

    private Notification convertToEntity(NotificationCreateDTO notificationDTO) {
        if (notificationDTO == null) return null;

        Employee employee = employeeRepository.findById(notificationDTO.getEmployeeID())
                .orElseThrow(() -> new BadRequestException("Employee not found with ID: " + notificationDTO.getEmployeeID()));

        return Notification.builder()
                .notificationID(notificationDTO.getNotificationID())
                .employee(employee)
                .message(notificationDTO.getMessage())
                .created(notificationDTO.getCreated() != null ? notificationDTO.getCreated() : LocalDateTime.now())
                .isRead(notificationDTO.getIsRead() != null ? notificationDTO.getIsRead() : false) // Default to unread
                .relatedEntityType(notificationDTO.getRelatedEntityType())
                .relatedEntityID(notificationDTO.getRelatedEntityID())
                .build();
    }

    @Override
    @Transactional
    public NotificationResponseDTO createNotification(NotificationCreateDTO notificationDTO) {
        Notification notification = convertToEntity(notificationDTO);
        Notification savedNotification = notificationRepository.save(notification);
        return convertToResponseDto(savedNotification);
    }

    @Override
    public Optional<NotificationResponseDTO> getNotificationById(Integer id) {
        return notificationRepository.findById(id).map(this::convertToResponseDto);
    }

    @Override
    public List<NotificationResponseDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponseDTO> getNotificationsByEmployeeId(Integer employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee not found with ID: " + employeeId);
        }
        return notificationRepository.findByEmployee_EmployeeID(employeeId).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponseDTO> getUnreadNotificationsByEmployeeId(Integer employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee not found with ID: " + employeeId);
        }
        return notificationRepository.findByEmployee_EmployeeIDAndIsRead(employeeId, false).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NotificationResponseDTO updateNotification(Integer id, NotificationCreateDTO notificationDetailsDTO) {
        Notification notificationToUpdate = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + id));

        // Update relationship if employee ID is provided
        if (notificationDetailsDTO.getEmployeeID() != null &&
                !notificationToUpdate.getEmployee().getEmployeeID().equals(notificationDetailsDTO.getEmployeeID())) {
            Employee newEmployee = employeeRepository.findById(notificationDetailsDTO.getEmployeeID())
                    .orElseThrow(() -> new BadRequestException("Employee not found with ID: " + notificationDetailsDTO.getEmployeeID()));
            notificationToUpdate.setEmployee(newEmployee);
        }

        // Update scalar fields
        notificationToUpdate.setMessage(notificationDetailsDTO.getMessage() != null ? notificationDetailsDTO.getMessage() : notificationToUpdate.getMessage());
        notificationToUpdate.setCreated(notificationDetailsDTO.getCreated() != null ? notificationDetailsDTO.getCreated() : notificationToUpdate.getCreated());
        notificationToUpdate.setIsRead(notificationDetailsDTO.getIsRead() != null ? notificationDetailsDTO.getIsRead() : notificationToUpdate.getIsRead());
        notificationToUpdate.setRelatedEntityType(notificationDetailsDTO.getRelatedEntityType() != null ? notificationDetailsDTO.getRelatedEntityType() : notificationToUpdate.getRelatedEntityType());
        notificationToUpdate.setRelatedEntityID(notificationDetailsDTO.getRelatedEntityID() != null ? notificationDetailsDTO.getRelatedEntityID() : notificationToUpdate.getRelatedEntityID());

        Notification updatedNotification = notificationRepository.save(notificationToUpdate);
        return convertToResponseDto(updatedNotification);
    }

    @Override
    @Transactional
    public NotificationResponseDTO markNotificationAsRead(Integer id) {
        Notification notificationToUpdate = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + id));

        notificationToUpdate.setIsRead(true);
        Notification updatedNotification = notificationRepository.save(notificationToUpdate);
        return convertToResponseDto(updatedNotification);
    }

    @Override
    public void deleteNotification(Integer id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found with ID: " + id);
        }
        notificationRepository.deleteById(id);
    }
}