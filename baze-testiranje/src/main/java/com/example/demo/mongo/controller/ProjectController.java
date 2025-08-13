package com.example.demo.mongo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.ProjectDTO;
import com.example.demo.mongo.entities.Project;
import com.example.demo.mongo.service.ProjectService;
import com.example.demo.neo4j.entities.ProjectNode;
import com.example.demo.neo4j.service.ProjectNodeService;

@RestController
@RequestMapping("/projects")
@CrossOrigin
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectNodeService projectNodeService;

    public ProjectController(ProjectService projectService,
    						ProjectNodeService projectNodeService) {
        this.projectService = projectService;
        this.projectNodeService=projectNodeService;
    }

    @GetMapping
    public List<Project> getAll() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getById(@PathVariable String id) {
    	Optional<Project> mongoProjectOpt = projectService.getProjectById(id);
        Optional<ProjectNode> neo4jProjectOpt = projectNodeService.getProjectById(id);

        if (mongoProjectOpt.isEmpty() || neo4jProjectOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ProjectDTO dto = new ProjectDTO(mongoProjectOpt.get(), neo4jProjectOpt.get());
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public Project create(@RequestBody Project project) {
    	Project savedProject = projectService.createProject(project);

    	ProjectNode projectNode = new ProjectNode(savedProject.getId());
    	projectNodeService.createProject(projectNode);

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
