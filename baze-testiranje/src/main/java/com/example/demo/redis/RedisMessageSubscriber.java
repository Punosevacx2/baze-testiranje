package com.example.demo.redis;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.mongo.entities.Notification;
import com.example.demo.mongo.entities.ProjectMessage;
import com.example.demo.websocket.WebSocketNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.nio.charset.StandardCharsets;

@Component
public class RedisMessageSubscriber implements MessageListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // WebSocket template

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("RedisMessageSubscriber je pozvan!");
        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            ProjectMessage chatMessage = objectMapper.readValue(json, ProjectMessage.class);
            System.out.println("Primljena poruka: " + chatMessage.getContent());
            messagingTemplate.convertAndSend("/topic/project/" + chatMessage.getProjectId(), chatMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

