package com.bexos.backend.services;

import com.bexos.backend.entitites.Notification;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class NotificationService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String userId) {
        SseEmitter emitter = new SseEmitter(60000L);

        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError(e -> {
            emitter.completeWithError(e);
            emitters.remove(userId);
        });

        try {
            System.out.println("Subscribing to " + emitter);
            emitter.send(SseEmitter.event().name("INIT"));
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

    public void notifyUser(String userId, Notification message) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(message));
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
}
