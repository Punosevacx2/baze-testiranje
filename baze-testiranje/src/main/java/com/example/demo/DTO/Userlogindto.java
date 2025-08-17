package com.example.demo.DTO;

public class Userlogindto {
	private String id;
    private String username;
    private String email;
    private String role;

    public Userlogindto(String id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
