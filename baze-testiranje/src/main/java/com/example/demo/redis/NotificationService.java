package com.example.demo.redis;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.mongo.entities.Notification;
import com.example.demo.mongo.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationPublisher notificationPublisher;

    public NotificationService(NotificationRepository notificationRepository,
                               NotificationPublisher notificationPublisher) {
        this.notificationRepository = notificationRepository;
        this.notificationPublisher = notificationPublisher;
    }

    public Notification sendNotification(String recipientId, String message, String type) {
        Notification notification = new Notification(recipientId, message, type);
        Notification saved = notificationRepository.save(notification);

        // Pub/Sub za real-time
        notificationPublisher.publish("notifications", saved);

        return saved;
    }

    public List<Notification> getUserNotifications(String recipientId) {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(recipientId);
    }

    public void markAsRead(String notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
}
