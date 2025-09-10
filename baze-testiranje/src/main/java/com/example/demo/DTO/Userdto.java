package com.example.demo.DTO;

import java.io.Serializable;

import com.example.demo.mongo.entities.User;
import com.example.demo.neo4j.entities.UserNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Userdto implements Serializable{

	private User user;
    private UserNode userNode;

    

    

	public User getUser() {
        return user;
    }

    public UserNode getUserNode() {
        return userNode;
    }
	
}
