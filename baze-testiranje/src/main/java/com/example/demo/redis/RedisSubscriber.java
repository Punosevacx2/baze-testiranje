package com.example.demo.redis;


import org.springframework.stereotype.Component;

import com.example.demo.mongo.entities.Notification;
import com.example.demo.websocket.WebSocketNotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Component
public class RedisSubscriber {

    private final SimpMessagingTemplate messagingTemplate;

    public RedisSubscriber(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void onMessage(Notification notification) {
        // šalje poruku na WebSocket destinaciju korisniku
        messagingTemplate.convertAndSendToUser(
                notification.getRecipientId(),
                "/queue/notifications",
                notification
        );
    }
}