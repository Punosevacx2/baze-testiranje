package com.example.demo.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestUserDTO
{
	private String email;
	private String username;   // display ime; Security username = email
	private String password;
	private String role;       // npr. "ADMIN" ili "USER"; može biti null
}
