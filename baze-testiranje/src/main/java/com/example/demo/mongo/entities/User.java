package com.example.demo.mongo.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Document(collection = "users")
public class User implements UserDetails
{

	@Id
	private String id;

	@Indexed(unique = true)
	private String email;

	// (može ti trebati kao “display name”, ali Spring Security username vraćamo = email)
	private String username;

	private String firstName;
	private String lastName;
	private String country;
	private String city;
	private String postalCode;

	private String password;

	// ❗️Ne koristiti Optional<Role> kao polje u entitetu
	private Role role;  // jedna rola po korisniku (USER, ADMIN, ...)

	public User()
	{
	}

	public User(String username, String email, String password, Role role)
	{
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	// ===== UserDetails =====

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		if (role == null || role.getName() == null)
			return List.of();
		String name = role.getName().trim();
		if (name.isEmpty())
			return List.of();

		// Normalizuj na ROLE_ prefiks tačno jednom, bez dupliranja
		String springRole = name.regionMatches(true, 0, "ROLE_", 0, 5) ?
				"ROLE_" + name.substring(5).trim().toUpperCase() :
				"ROLE_" + name.toUpperCase();

		return List.of(new SimpleGrantedAuthority(springRole));
	}

	// username za Security = email (pošto ti je JWT subject = email)
	@Override
	public String getUsername()
	{
		return this.email;
	}

	@Override
	public String getPassword()
	{
		return this.password;
	}

	@Override
	public boolean isAccountNonExpired()
	{
		return true;
	}

	@Override
	public boolean isAccountNonLocked()
	{
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		return true;
	}

	@Override
	public boolean isEnabled()
	{
		return true;
	}

	// ===== Getteri / setteri =====

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getDisplayUsername()
	{
		return username;
	}

	public void setDisplayUsername(String username)
	{
		this.username = username;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String v)
	{
		this.firstName = v;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String v)
	{
		this.lastName = v;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String v)
	{
		this.country = v;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String v)
	{
		this.city = v;
	}

	public String getPostalCode()
	{
		return postalCode;
	}

	public void setPostalCode(String v)
	{
		this.postalCode = v;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Role getRole()
	{
		return role;
	}

	public void setRole(Role role)
	{
		this.role = role;
	}
}
