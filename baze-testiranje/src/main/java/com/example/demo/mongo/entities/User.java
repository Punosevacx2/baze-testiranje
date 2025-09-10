package com.example.demo.mongo.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.mongo.entities.Role;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Document(collection = "users")
public class User implements UserDetails {

	 @Id
	    private String id;

	    private String email;
	    private String username;
	    private String firstName;
	    private String lastName;
	    private String country;
	    private String city;
	    private String postalCode;
	    private String password;

	    private Role roles;

    public User() {}

    public User(String username, String email, String password,Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = role;
    }

    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.roles.getName()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

	public void setEmail(String email2) {
		email=email2;
		
	}

	public void setUsername(String username2) {
	username=username2;
		
	}

	public void setFirstName(String object) {
		firstName=object;
		
	}

	public void setLastName(String object) {
		lastName=object;
		
	}

	public void setCountry(String string) {
		country=string;
		
	}

	public void setCity(String string) {
		city=string;
		
	}

	public void setPostalCode(String string) {
		postalCode=string;
		
	}

	public void setPassword(String encode) {
		password=encode;
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public void setId(String id2) {
		// TODO Auto-generated method stub
		id=id2;
	}

	public void setRoles(Role roles) {
		// TODO Auto-generated method stub
		this.roles=roles;
	}

	public Role getRoles() {
		// TODO Auto-generated method stub
		return roles;
	}

	

	
}
