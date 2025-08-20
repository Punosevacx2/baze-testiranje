package com.example.demo.DTO;


import java.util.List;

import com.example.demo.mongo.entities.Role;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestUserDTO {

    protected String email;

    protected String username;


    protected String password;

    protected Role roles;


	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	public String getEmail() {
		// TODO Auto-generated method stub
		return email;
	}

	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	public Role getRoles() {
		// TODO Auto-generated method stub
		return roles;
	}

}