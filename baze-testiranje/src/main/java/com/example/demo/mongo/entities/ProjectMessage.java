package com.example.demo.mongo.entities;

import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "chat_messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMessage {
    private String projectId;  // ID projekta kojem poruka pripada
    private String senderId;   // ID korisnika koji šalje
    private String senderName; // opcionalno, za prikaz
    private String content;    // tekst poruke
    private LocalDateTime timestamp;
	public void setProjectId(String projectId2) {
		// TODO Auto-generated method stub
		projectId=projectId2;
	}
	public String getProjectId() {
		// TODO Auto-generated method stub
		return projectId;
	}
	public void setTimestamp(LocalDateTime now) {
		// TODO Auto-generated method stub
		timestamp=now;
	}
	
	public void setSenderId(String senderId2) {
		// TODO Auto-generated method stub
		senderId=senderId2;
	}
	
	
	public void setSenderName(String senderName2) {
		// TODO Auto-generated method stub
		senderName=senderName2;
		
	}
	public void setContent(String text) {
		// TODO Auto-g
		this.content=text;
		
	}
	public String getContent() {
		// TODO Auto-generated method stub
		return content;
	}
}
