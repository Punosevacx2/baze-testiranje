package com.example.demo.DTO;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDTO implements Serializable  {
	
    private static final long serialVersionUID = -5055972220079829960L;

	private String accessToken;
    
    private String refreshToken;

    private long expiresIn;

    
    public LoginResponseDTO(String accessToken, String refreshToken, long expiresIn) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.expiresIn = expiresIn;
	}

	@Override
	public String toString() {
		return "LoginResponseDTO [accessToken=" + accessToken + ", refreshToken=" + refreshToken + ", expiresIn="
				+ expiresIn + "]";
	}

}
