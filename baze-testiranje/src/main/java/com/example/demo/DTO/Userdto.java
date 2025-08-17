package com.example.demo.DTO;

import com.example.demo.mongo.entities.User;
import com.example.demo.neo4j.entities.UserNode;

public class Userdto {

	private User user;
    private UserNode userNode;

    public Userdto(User user, UserNode userNode) {
        this.user = user;
        this.userNode = userNode;
    }

    public User getUser() {
        return user;
    }

    public UserNode getUserNode() {
        return userNode;
    }
	
}
