package com.example.demo.mongo.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "taskstatus")
public class TaskStatus {

	
	    @Id
	    private String id;

	    private String name; // sada tipa enumeracije

	    

		public String getName() {
	        return name;
	    }

	    public void	 setName(String name) {
	        this.name = name;
	    }

		
	

	
}
