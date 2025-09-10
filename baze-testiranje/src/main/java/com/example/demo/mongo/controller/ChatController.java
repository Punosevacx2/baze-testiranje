package com.example.demo.mongo.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.example.demo.mongo.entities.ProjectMessage;
import com.example.demo.mongo.repository.ChatMessageRepository;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatMessageRepository repository;

    public ChatController(ChatMessageRepository repository) {
        this.repository = repository;
    }

    
    @GetMapping("/projects/{id}/messages")
    public List<ProjectMessage> getMessages(@PathVariable String id) {
        return repository.findByProjectId(id);
    }
    @PostMapping("/projects/{id}/messages")
    public ProjectMessage sendMessage(
            @PathVariable String id,
            @RequestBody ProjectMessage message) {
        message.setProjectId(id);
        message.setTimestamp(LocalDateTime.now());
       
        return repository.save(message);
    }
}
