package com.bexos.backend.controllers;

import com.bexos.backend.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe/{userId}", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(@PathVariable String userId) {
        return notificationService.subscribe(userId);
    }

    @PostMapping("/notify/{userId}")
    public void notifyUser(@PathVariable String userId, @RequestBody String message) {
        System.out.println("Message " + message);
        notificationService.notifyUser(userId, message);
    }

    @PostMapping("/notify")
    public void notifyAllUsers(@RequestBody String message) {
        System.out.println("Message " + message);
        notificationService.notifyAllUsers(message);
    }
}
