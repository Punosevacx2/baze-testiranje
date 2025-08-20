package com.example.demo.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseUserDTO
{
	private String id;
	private String email;
	private String username;      // display ime
	private String role;          // "USER" / "ADMIN"
	private String userNodeId;    // ili ceo mini-DTO, ali ne direktan Neo4j entitet
}
