package com.example.demo.DTO;


import java.util.List;
import java.util.Optional;

import com.example.demo.mongo.entities.Role;
import com.example.demo.neo4j.entities.UserNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUserDTO extends RequestUserDTO {

    private String id;

    private Optional<UserNode> userNode;
    
	public void setId(String id2) {
		id=id2;
		
	}

	public void setUsername(String username) {
		// TODO Auto-generated method stub
		this.username=username;
	}

	public void setEmail(String email) {
		// TODO Auto-generated method stub
		this.email=email;
		
	}


	public void setRole(Role roles) {
		// TODO Auto-generated method stub
		this.roles=roles;
	}

	public void setPasswort(String password) {
		// TODO Auto-generated method stub
		this.password=password;
	}

	public void setUserNode(Optional<UserNode> userNode2) {
		// TODO Auto-generated method stub
		userNode=userNode2;
		
	}
    
}