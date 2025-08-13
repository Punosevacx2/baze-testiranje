package com.example.demo.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.mongo.entities.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ChatMessageSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public ChatMessageSubscriber(SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // Deserialize JSON string iz Redis poruke u ChatMessage objekat
            String json = new String(message.getBody());
            ChatMessage chatMessage = objectMapper.readValue(json, ChatMessage.class);

            // Pošalji poruku WebSocket klijentima na odgovarajući task kanal
            messagingTemplate.convertAndSend(
                "/topic/chat/" + chatMessage.getTaskId(),
                chatMessage
            );
            System.out.println("Stigla poruka: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}