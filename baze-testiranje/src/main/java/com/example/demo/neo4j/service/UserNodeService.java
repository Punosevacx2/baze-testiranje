package com.example.demo.neo4j.service;


import com.example.demo.neo4j.entities.UserNode;
import com.example.demo.neo4j.repository.UserNodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserNodeService {

    private final UserNodeRepository userNodeRepository;

    public UserNodeService(UserNodeRepository userNodeRepository) {
        this.userNodeRepository = userNodeRepository;
    }

    public List<UserNode> getAllUsers() {
        return userNodeRepository.findAll();
    }

    public Optional<UserNode> getUserById(String id) {
        return userNodeRepository.findById(id);
    }

    public UserNode createUserNode(UserNode userNode) {
        return userNodeRepository.save(userNode);
    }

    public UserNode updateUserNode(String id, UserNode updatedNode) {
        updatedNode.setId(id);
        return userNodeRepository.save(updatedNode);
    }

    public void deleteUserNode(String id) {
        userNodeRepository.deleteById(id);
    }

    
}

