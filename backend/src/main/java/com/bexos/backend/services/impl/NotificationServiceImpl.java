package com.bexos.backend.services.impl;

import com.bexos.backend.dto.NotificationResponse;
import com.bexos.backend.entitites.Notification;
import com.bexos.backend.entitites.User;
import com.bexos.backend.handler.BadRequestsException;
import com.bexos.backend.handler.NotFoundException;
import com.bexos.backend.repositories.NotificationRepository;
import com.bexos.backend.repositories.UserRepository;
import com.bexos.backend.services.NotificationService;
import com.bexos.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public SseEmitter subscribe() {
        User user = userService.getAuthenticatedUser();
        SseEmitter emitter = new SseEmitter(3600 * 1000L);

        emitters.put(String.valueOf(user.getId()), emitter);

        emitter.onCompletion(() -> emitters.remove(String.valueOf(user.getId())));
        emitter.onTimeout(() -> emitters.remove(String.valueOf(user.getId())));
        emitter.onError(e -> {
            emitter.completeWithError(e);
            emitters.remove(String.valueOf(user.getId()));
        });

        try {
            Notification notification = Notification.builder().title("Connection").message("Connection successful").build();
            emitter.send(SseEmitter.event().data(notification));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return emitter;
    }

    public void notifyUser(Integer userId, String message) {
        SseEmitter emitter = emitters.get(String.valueOf(userId));
        if (emitter != null) {
            try {
                User user = userRepository.findById(userId).orElseThrow(
                        () -> new NotFoundException("User not found")
                );
                Notification notification = notificationRepository.save(Notification.builder().title("New message").message(message).user(user).build());
                NotificationResponse response = new NotificationResponse(notification);
                emitter.send(SseEmitter.event().data(response));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(String.valueOf(userId));
            } catch (IllegalStateException e) {
                emitters.remove(String.valueOf(userId));
            }
        }
    }

    public void notifyUser(Integer userId, NotificationResponse notifications) {
        SseEmitter emitter = emitters.get(String.valueOf(userId));
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().data(notifications));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(String.valueOf(userId));
            }
        }
    }

    public void notifyAllUsers(String message) {
        emitters.forEach((userId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name("notification").data(message));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(userId);
            }
        });
    }

    public NotificationResponse getNotification(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BadRequestsException("Notification do not exist"));
        return new NotificationResponse(notification);
    }
}

