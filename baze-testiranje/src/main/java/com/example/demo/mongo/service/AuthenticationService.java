package com.example.demo.mongo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.RequestUserDTO;
import com.example.demo.DTO.Userlogindto;
import com.example.demo.mongo.entities.User;
import com.example.demo.mongo.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository,
                                 AuthenticationManager authenticationManager,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RequestUserDTO requestUserDto) {
        User user = new User( requestUserDto.getUsername(),requestUserDto.getEmail(),
                passwordEncoder.encode(requestUserDto.getPassword()), requestUserDto.getRoles());
       
        return userRepository.save(user);
    }

    public User authenticate(Userlogindto loginUserDTO) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginUserDTO.getEmail(), loginUserDTO.getPassword()));
        User user = userRepository.findByEmail(loginUserDTO.getEmail()).orElseThrow();
        return user;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }
}