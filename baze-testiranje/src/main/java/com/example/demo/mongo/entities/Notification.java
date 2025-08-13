package com.example.demo.mongo.entities;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;
    private String recipientId; // kome ide notifikacija
    private String message;
    private boolean read;
    private Instant createdAt;
    private String type; // tip poruke, npr. TASK_ASSIGNED, CHAT_MESSAGE, SYSTEM_ALERT

    public Notification() {}

    public Notification(String recipientId, String message, String type) {
        this.recipientId = recipientId;
        this.message = message;
        this.type = type;
        this.read = false;
        this.createdAt = Instant.now();
    }

	public String getRecipientId() {
		
		return recipientId;
	}

	public void setRead(boolean b) {
		read =b;
	}

    // getteri i setteri
    // ...
}