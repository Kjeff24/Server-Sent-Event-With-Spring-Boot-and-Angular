package com.bexos.backend.dto;

import com.bexos.backend.entitites.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Integer id;
    private String event;
    private String title;
    private String message;
    private boolean read;
    private String timeAgo;

    public NotificationResponse(Notification notification) {
        this.id = notification.getId();
        this.event = notification.getEvent();
        this.title = notification.getTitle();
        this.message = notification.getMessage();
        this.read = notification.isRead();
        this.timeAgo = calculateTimeAgo(notification.getCreatedAt());
    }

    private String calculateTimeAgo(LocalDateTime notificationDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(notificationDate, now);

        if (duration.toMinutes() < 1) {
            return duration.getSeconds() + "s ago";
        } else if (duration.toHours() < 1) {
            return duration.toMinutes() + "m ago";
        } else if (duration.toDays() < 1) {
            return duration.toHours() + "h ago";
        } else if (duration.toDays() == 1) {
            return "1 day ago";
        } else {
            return duration.toDays() + " days ago";
        }
    }
}
