package com.example.demo.mongo.entities;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.demo.enumeration.TaskStatuss;

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
    
    private TaskStatuss status;           
    
    private LocalDate deadline;
	
    
    public void setId(String id2) {
		id=id2;
	}


	public String getId() {
		return id;
	}



	public TaskStatuss getStatus() {
		// TODO Auto-generated method stub
		return status;
	}


	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}
}
