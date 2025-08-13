package com.example.demo.redis;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.mongo.entities.ChatMessage;
import com.example.demo.mongo.repository.ChatMessageRepository;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    // Sačuvaj novu chat poruku u bazu
    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    // Dohvati sve poruke za dati task (po taskId)
    public List<ChatMessage> getMessagesByTaskId(String taskId) {
        return chatMessageRepository.findByTaskIdOrderByTimestampAsc(taskId);
    }

	public List<String> getActiveUsers(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

    // Opcionalno: metoda za dobijanje aktivnih korisnika (ako pratiš to)
    // public List<String> getActiveUsers(String taskId) { ... }
}
