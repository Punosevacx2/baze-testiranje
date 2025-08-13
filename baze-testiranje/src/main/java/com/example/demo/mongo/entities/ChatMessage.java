package com.example.demo.mongo.entities;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;

    private String taskId;      // task kojem poruka pripada
    private String senderId;    // ko je poslao poruku
    private String content;     // sadržaj poruke
    private Instant timestamp;  // vreme slanja
	public void setTimestamp(Instant now) {
		timestamp=now;
	}
	public String getTaskId() {
		return taskId;
	}

}
