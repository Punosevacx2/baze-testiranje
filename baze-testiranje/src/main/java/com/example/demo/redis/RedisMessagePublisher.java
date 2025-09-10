package com.example.demo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.mongo.entities.ProjectMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RedisMessagePublisher {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void publish(String channel, Object message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            redisTemplate.convertAndSend(channel, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}