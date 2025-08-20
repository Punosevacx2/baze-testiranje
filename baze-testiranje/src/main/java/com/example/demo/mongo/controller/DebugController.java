package com.example.demo.mongo.controller;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DebugController
{

	@GetMapping("/ping")
	public Map<String, String> ping()
	{
		return Map.of("ok", "pong");
	}

	@GetMapping("/whoami")
	public Object whoami(Authentication auth)
	{
		if (auth == null)
			return Map.of("auth", "anonymous");
		return Map.of("name", auth.getName(), "authorities", auth.getAuthorities().toString());
	}
}
