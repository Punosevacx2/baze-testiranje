package com.example.demo.mongo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.mongo.entities.Role;
import com.example.demo.mongo.entities.User;
import com.example.demo.mongo.repository.RoleRepository;
import com.example.demo.mongo.repository.UserRepository;
import com.example.demo.mongo.service.UserService;
import com.example.demo.neo4j.entities.UserNode;
import com.example.demo.neo4j.service.UserNodeService;
import com.example.demo.DTO.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoinController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserNodeService userNodeService;
    
    public LoinController(UserRepository userRepository,RoleRepository roleRepository,PasswordEncoder passwordEncoder,UserNodeService userNodeService) {
    			this.userRepository=userRepository;
    			this.roleRepository=roleRepository;
    			this.passwordEncoder=passwordEncoder;
    			this.userNodeService=userNodeService;
}

    // Registracija korisnika
    @PostMapping("/register")
    public ResponseEntity<Userlogindto> registerUser(@RequestBody User user) {
        // Provera da li korisnik postoji
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Postavi rolu
        String roleName = user.getRoles().getName(); // ROLE_USER ili ROLE_MANAGER
        Role role = roleRepository.findByName(roleName);

        user.setRoles(role);
        userRepository.save(user);
        UserNode userNode = new UserNode(user.getId());
        userNodeService.createUserNode(userNode);

        Userlogindto response = new Userlogindto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().getName()
        );
         return ResponseEntity.ok(response);
    }

    // Login korisnika
    @PostMapping("/login")
    public ResponseEntity<Userlogindto> loginUser(@RequestBody String email,@RequestBody String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Vrati korisnika sa rolom Userlogindto response = new Userlogindto(
        Userlogindto response = new Userlogindto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getRoles().getName()
);
 return ResponseEntity.ok(response);
    }
}
