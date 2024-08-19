package com.bexos.backend.services;


import com.bexos.backend.dto.NotificationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Service
public interface NotificationService {

    SseEmitter subscribe();

    void notifyUser(Integer userId, String message);
    void notifyUser(Integer userId, NotificationResponse message);

    void notifyAllUsers(String message);

    NotificationResponse getNotification(Integer notificationId);
}

    