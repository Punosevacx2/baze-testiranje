package com.example.demo.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public NotificationPublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publish(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }
}