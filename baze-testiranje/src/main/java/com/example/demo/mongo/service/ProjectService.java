package com.example.demo.mongo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.mongo.entities.Project;
import com.example.demo.mongo.repository.ProjectRepository;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(String id) {
        return projectRepository.findById(id);
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(String id, Project updatedProject) {
        updatedProject.setId(id);
        return projectRepository.save(updatedProject);
    }

    public void deleteProject(String id) {
        projectRepository.deleteById(id);
    }
}
