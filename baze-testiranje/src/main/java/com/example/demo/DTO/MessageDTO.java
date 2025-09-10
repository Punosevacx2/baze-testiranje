package com.example.demo.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private String projectId;
    private String senderUserId;
    private String content;
    private String senderName;
    private LocalDateTime timestamp;
	public String getProjectId() {
		// TODO Auto-generated method stub
		return projectId;
	}
	public String getSenderId() {
		// TODO Auto-generated method stub
		return senderUserId;
	}
	public String getContent() {
		// TODO Auto-generated method stub
		return content;
	}
	public LocalDateTime getTimestamp() {
		// TODO Auto-generated method stub
		return timestamp;
	}
	public String getSenderName() {
		// TODO Auto-generated method stub
		return senderName;
	}
	
}
