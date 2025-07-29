package com.example.demo.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationPublisher {

    private final RedisTemplate<String, String> redisTemplate;

    public NotificationPublisher(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publish(String message) {
        redisTemplate.convertAndSend("notifications", message);
    }
}
