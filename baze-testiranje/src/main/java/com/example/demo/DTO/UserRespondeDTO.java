package com.example.demo.DTO;

import com.example.demo.mongo.entities.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRespondeDTO {



	private String id;

    private String email;
    private String username;
    
	
}
