package com.example.demo.mongo.entities;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.demo.enumeration.TaskStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    private String id;
    
    private String title;
    
    private String description;
    
    private String assignedToUserId; // User.id
    
    private String projectId;        // Project.id
    
    private TaskStatus status;           
    
    private LocalDate deadline;
	
    
    public void setId(String id2) {
		id=id2;
	}


	public String getId() {
		return id;
	}
}
