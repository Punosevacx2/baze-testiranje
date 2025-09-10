package com.example.demo.mongo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.Projectdto;
import com.example.demo.mongo.entities.Project;
import com.example.demo.mongo.entities.User;
import com.example.demo.mongo.service.ProjectService;
import com.example.demo.neo4j.entities.ProjectNode;
import com.example.demo.neo4j.entities.UserNode;
import com.example.demo.neo4j.service.ProjectNodeService;
import com.example.demo.neo4j.service.RelationService;
import com.example.demo.neo4j.service.UserNodeService;

@RestController
@RequestMapping("/projects")
@CrossOrigin
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectNodeService projectNodeService;
    private final UserNodeService userNodeService;
    private final RelationService relationService;

    public ProjectController(ProjectService projectService,
    						ProjectNodeService projectNodeService,
    						UserNodeService userNodeService,
    						RelationService relationService) {
        this.projectService = projectService;
        this.projectNodeService=projectNodeService;
        this.userNodeService=userNodeService;
        this.relationService=relationService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Project> getAll() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projectdto> getById(@PathVariable String id) {
    	Optional<Project> mongoProjectOpt = projectService.getProjectById(id);
        Optional<ProjectNode> neo4jProjectOpt = projectNodeService.getProjectById(id);

        if (mongoProjectOpt.isEmpty() || neo4jProjectOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Projectdto dto = new Projectdto(mongoProjectOpt.get(), neo4jProjectOpt.get());
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public Project create(@RequestBody Project project) {
        // 1. Sačuvaj projekat u MongoDB
        Project savedProject = projectService.createProject(project);

        // 2. Kreiraj čvor projekta u Neo4j
        ProjectNode projectNode = new ProjectNode(savedProject.getId());
        projectNodeService.createProject(projectNode);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        

        // 4. Napravi vezu u Neo4j (MENADŽER -> PROJEKAT)
        relationService.addUserToProject(currentUser.getId(), projectNode.getId());

        return savedProject;
    }
    
    

    @PutMapping("/{id}")
    public Project update(@PathVariable String id, @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        projectService.deleteProject(id);
        projectNodeService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
