package com.ead.notification.controllers;

import com.ead.notification.dtos.NotificationDto;
import com.ead.notification.models.NotificationModel;
import com.ead.notification.services.NotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
public class UserNotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping(path = "/users/{userId}/notifications")
    public ResponseEntity<Page<NotificationModel>> getAllNotificationsByUser(@PathVariable UUID userId,
                                                                             @PageableDefault(page = 0, size = 10, sort = "notificationId", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<NotificationModel> notificationModelPage = notificationService.findAllNotificationsByUser(userId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(notificationModelPage);
    }

    @PutMapping(path = "/users/{userId}/notifications/{notificationId}")
    public ResponseEntity<Object> updateNotification(@PathVariable UUID userId, @PathVariable UUID notificationId, @RequestBody @Valid NotificationDto notificationDto) {
        Optional<NotificationModel> notificationModelOptional = notificationService.findByNotificationIdAndUserId(notificationId, userId);
        if (notificationModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification not found!");
        }
        notificationModelOptional.get().setNotificationStatus(notificationDto.getNotificationStatus());
        NotificationModel notificationModelSaved = notificationService.saveNotification(notificationModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body(notificationModelSaved);
    }
}
