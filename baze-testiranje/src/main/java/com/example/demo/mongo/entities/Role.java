package com.example.demo.mongo.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
public class Role {
    @Id
    private String id;
    private String name; // npr. ROLE_USER, ROLE_ADMIN
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
}
