package com.workin.personnelevaluationsystem.controller;

import com.workin.personnelevaluationsystem.dto.NotificationCreateDTO;
import com.workin.personnelevaluationsystem.dto.NotificationResponseDTO;
import com.workin.personnelevaluationsystem.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createNotification(@Valid @RequestBody NotificationCreateDTO notificationDTO) {
        NotificationResponseDTO createdNotification = notificationService.createNotification(notificationDTO);
        return new ResponseEntity<>(createdNotification, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> getNotificationById(@PathVariable Integer id) {
        return notificationService.getNotificationById(id)
                .map(notificationDTO -> new ResponseEntity<>(notificationDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotifications() {
        List<NotificationResponseDTO> notifications = notificationService.getAllNotifications();
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByEmployeeId(@PathVariable Integer employeeId) {
        List<NotificationResponseDTO> notifications = notificationService.getNotificationsByEmployeeId(employeeId);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}/unread")
    public ResponseEntity<List<NotificationResponseDTO>> getUnreadNotificationsByEmployeeId(@PathVariable Integer employeeId) {
        List<NotificationResponseDTO> notifications = notificationService.getUnreadNotificationsByEmployeeId(employeeId);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> updateNotification(@PathVariable Integer id, @Valid @RequestBody NotificationCreateDTO notificationDetailsDTO) {
        NotificationResponseDTO updatedNotification = notificationService.updateNotification(id, notificationDetailsDTO);
        return new ResponseEntity<>(updatedNotification, HttpStatus.OK);
    }

    @PatchMapping("/{id}/mark-read")
    public ResponseEntity<NotificationResponseDTO> markNotificationAsRead(@PathVariable Integer id) {
        NotificationResponseDTO updatedNotification = notificationService.markNotificationAsRead(id);
        return new ResponseEntity<>(updatedNotification, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Integer id) {
        notificationService.deleteNotification(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}