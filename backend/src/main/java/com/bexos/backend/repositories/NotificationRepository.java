package com.bexos.backend.repositories;

import com.bexos.backend.entitites.Notification;
import com.bexos.backend.entitites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserAndIsReadFalseOrderByCreatedAtDesc(User user);
}
