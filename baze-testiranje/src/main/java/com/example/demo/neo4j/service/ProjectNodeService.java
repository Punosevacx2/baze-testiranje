package com.example.demo.neo4j.service;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.neo4j.entities.ProjectNode;
import com.example.demo.neo4j.repository.ProjectNodeRepository;

@Service
public class ProjectNodeService {
	private final ProjectNodeRepository projectNodeRepository;

    public ProjectNodeService(ProjectNodeRepository projectNodeRepository) {
        this.projectNodeRepository = projectNodeRepository;
        
    }
	
	public ProjectNode createProject(ProjectNode project) {
        if (project.getTasks() == null) {
            project.setTasks(new HashSet<>());
        }
        if (project.getMembers() == null) {
            project.setMembers(new HashSet<>());
        }
        return projectNodeRepository.save(project);
    }

    public Optional<ProjectNode> getProjectById(String id) {
        return projectNodeRepository.findById(id);
    }

    public Iterable<ProjectNode> getAllProjects() {
        return projectNodeRepository.findAll();
    }

    public void deleteProject(String id) {
        projectNodeRepository.deleteById(id);
    }

}
