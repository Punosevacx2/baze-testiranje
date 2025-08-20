package com.example.demo.mongo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.RequestUserDTO;
import com.example.demo.DTO.Userlogindto;
import com.example.demo.mongo.entities.Role;
import com.example.demo.mongo.entities.User;
import com.example.demo.mongo.repository.RoleRepository;
import com.example.demo.mongo.repository.UserRepository;

import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public AuthenticationService(UserRepository userRepository,
                                 AuthenticationManager authenticationManager,
                                 PasswordEncoder passwordEncoder,
                                 RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository=roleRepository;
    }

    public User signup(RequestUserDTO req) {
        User user = new User();
        user.setDisplayUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        String requested = (req.getRole() == null || req.getRole().isBlank()) ? "USER" : req.getRole().trim();
        Role role = roleRepository.findByName(requested)
              .orElseGet(() -> roleRepository.save(new Role(null, requested)));

        user.setRole(role);
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
