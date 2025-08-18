package com.example.demo.mongo.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@Tag(name = "User management, login and logout", description = "Operations related to user lifecycle, login and logout")
public class LogoutController {

    private Set<String> tokenBlacklist = new HashSet<>();

    @Operation(summary = "User logout", description = "Logs out the user with the passed JWT token")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        blacklistToken(token);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }

    public void blacklistToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            tokenBlacklist.add(token);
        }
    }
}