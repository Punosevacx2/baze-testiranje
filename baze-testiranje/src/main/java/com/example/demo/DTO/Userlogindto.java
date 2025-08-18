package com.example.demo.DTO;

public class Userlogindto {
	private String id;
    private String username;
    private String email;
    private String password;
    private String role;

    public Userlogindto(String id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }
    @Override
    public String toString() {
        return "Userlogindto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
	public String getEmail() {
		// TODO Auto-generated method stub
		return email;
	}
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}
	
	
}
