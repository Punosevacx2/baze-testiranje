package com.example.demo.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.mongo.entities.ChatMessage;

@Component
public class ChatMessagePublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public ChatMessagePublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publishChatMessage(String taskId, ChatMessage message) {
        redisTemplate.convertAndSend("chat_" + taskId, message);
    }
}
