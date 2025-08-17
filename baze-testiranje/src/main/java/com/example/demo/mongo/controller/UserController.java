package com.example.demo.mongo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.Userdto;
import com.example.demo.mongo.entities.User;
import com.example.demo.mongo.service.UserService;
import com.example.demo.neo4j.entities.UserNode;
import com.example.demo.neo4j.service.UserNodeService;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final UserNodeService userNodeService;

    public UserController(UserService userService,
    						UserNodeService userNodeService) {
        this.userService = userService;
        this.userNodeService=userNodeService;
    }

 
    
    @GetMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Userdto> getById(@PathVariable String id) {
        Optional<User> mongoUserOpt = userService.getUserById(id);
        Optional<UserNode> neo4jUserOpt = userNodeService.getUserById(id);

        if (mongoUserOpt.isEmpty() || neo4jUserOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Userdto dto = new Userdto(mongoUserOpt.get(), neo4jUserOpt.get());
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public User create(@RequestBody User user) {
    	User savedUser = userService.createUser(user);

        UserNode userNode = new UserNode(savedUser.getId());
        userNodeService.createUserNode(userNode);

        return savedUser;
    }

    @PutMapping("/{id}")
    public User update(@PathVariable String id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.deleteUser(id);
        userNodeService.deleteUserNode(id);
        return ResponseEntity.noContent().build();
    }
}
