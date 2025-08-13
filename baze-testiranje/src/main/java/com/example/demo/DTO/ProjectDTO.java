package com.example.demo.DTO;

import com.example.demo.mongo.entities.Project;
import com.example.demo.neo4j.entities.ProjectNode;

public class ProjectDTO {
	private Project project;
    private ProjectNode projectNode;

    public ProjectDTO(Project project, ProjectNode projectNode) {
        this.project = project;
        this.projectNode = projectNode;
    }

    public Project getUser() {
        return project;
    }

    public ProjectNode getUserNode() {
        return projectNode;
    }
}
