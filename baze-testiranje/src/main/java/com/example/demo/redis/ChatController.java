package com.example.demo.redis;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.mongo.entities.ChatMessage;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatMessageService;

    public ChatController(ChatService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat/message")
  /*  public void processMessage(ChatMessage chatMessage) {
        // Kad primiš poruku, prosledi je svim pretplaćenima na /topic/chat/{taskId}
        String destination = "/topic/chat/" + chatMessage.getTaskId();
        messagingTemplate.convertAndSend(destination, chatMessage);
    }
    */
    
    // 1. Dohvatanje svih poruka za određeni task
    @GetMapping("/messages/{taskId}")
    public List<ChatMessage> getMessagesForTask(@PathVariable String taskId) {
        return chatMessageService.getMessagesByTaskId(taskId);
    }

    // 2. Slanje nove chat poruke (ako želiš REST putanju pored WebSocket-a)
    @PostMapping("/messages")
    public ChatMessage sendMessage(@RequestBody ChatMessage chatMessage) {
        return chatMessageService.saveMessage(chatMessage);
    }

    // 3. Opcionalno: Dobijanje aktivnih korisnika na određenom tasku (ako pratiš to)
    @GetMapping("/activeUsers/{taskId}")
    public List<String> getActiveUsersOnTask(@PathVariable String taskId) {
        return chatMessageService.getActiveUsers(taskId);
    }
}

