package com.bexos.backend.services;

import com.bexos.backend.controllers.NotificationController;
import com.bexos.backend.dto.NotificationResponse;
import com.bexos.backend.entitites.Notification;
import com.bexos.backend.entitites.User;
import com.bexos.backend.handler.BadRequestsException;
import com.bexos.backend.handler.NotFoundException;
import com.bexos.backend.repositories.NotificationRepository;
import com.bexos.backend.repositories.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public SseEmitter subscribe(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        SseEmitter emitter = new SseEmitter(3600 * 1000L);

        emitters.put(String.valueOf(userId), emitter);

        emitter.onCompletion(() -> emitters.remove(String.valueOf(userId)));
        emitter.onTimeout(() -> emitters.remove(String.valueOf(userId)));
        emitter.onError(e -> {
            emitter.completeWithError(e);
            emitters.remove(String.valueOf(userId));
        });

        try {
            System.out.println("Subscribing to " + emitter);
            List<NotificationResponse> notifications = notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user).stream()
                    .map(NotificationResponse::new)
                    .toList();
            emitter.send(SseEmitter.event().data(notifications));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return emitter;
    }

    public void notifyUser(String userId, String message) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                Gson gson = new Gson();

                // Create JSON object with event name and data
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("event", "notification");
                jsonObject.addProperty("message", message);

                // Convert JSON object to string
                String jsonMessage = gson.toJson(jsonObject);

                System.out.println("Notifying emitter " + emitter);
                emitter.send(SseEmitter.event().data(jsonMessage));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(userId);
            } catch (IllegalStateException e) {
                // This exception can occur if the emitter is already complete
                emitters.remove(userId);
            }
        }
    }

    public void notifyUser(String userId, List<NotificationResponse> messages) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                System.out.println("Notifying emitter " + emitter);
                emitter.send(SseEmitter.event().data(messages));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(userId);
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
