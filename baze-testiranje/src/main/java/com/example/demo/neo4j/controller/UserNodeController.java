package com.example.demo.neo4j.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.mongo.entities.Project;
import com.example.demo.mongo.service.ProjectService;
import com.example.demo.neo4j.entities.ProjectNode;
import com.example.demo.neo4j.service.UserNodeService;
import com.example.demo.neo4j.entities.*;

@RestController
@RequestMapping("/usersnode")
public class UserNodeController {

    private final UserNodeService userNodeService;
    private final ProjectService projectService;

    public UserNodeController(UserNodeService userNodeService,
    		ProjectService projectService) {
        this.userNodeService = userNodeService;
        this.projectService=projectService;
    }

    // Endpoint koji vraća sve projekte za datog korisnika
    @GetMapping("/{userId}/projects")
    public ResponseEntity<List<Project>> getProjectsForUser(@PathVariable String userId) {
        // 1. Dobij ID-eve projekata iz Neo4j
        Set<ProjectNode> projectNodes = userNodeService.getProjectsForUser(userId);
        System.out.println(userId);
        Set<String> projectIds = projectNodes.stream()
                .map(p -> p.getId()) // uzimamo samo ID-eve
                .collect(Collectors.toSet());
        System.out.println(projectIds);
        // 2. Dobij full Project objekte iz MongoDB
        List<Project> projects = projectService.findAllById(projectIds);
        System.out.println(projects);
        return ResponseEntity.ok(projects);
    }

	
    
    }
