package com.example.demo.mongo.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    private String id;
    
    private String name;
    
    private List<String> memberIds; // Lista ID-jeva korisnika (User.id)
	
    
    public void setId(String id2) {
		id=id2;
		
	}


	public String getId() {
		return id;
	}
}
