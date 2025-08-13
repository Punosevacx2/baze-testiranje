package com.example.demo.neo4j.entities;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("Task")
public class TaskNode {

    @Id
    private String id;

    @Relationship(type = "DEPENDS_ON")
    private Set<TaskNode> dependencies = new HashSet<>();
    
    @Relationship(type = "PART_OF")
    private ProjectNode project;
    
    @Relationship(type = "WORKS_ON")
    private Set<ProjectNode> projects = new HashSet<>();

    // Constructors, getters, setters

    public TaskNode() {}

    public TaskNode(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Set<TaskNode> getDependencies() { return dependencies; }
    public void setDependencies(Set<TaskNode> dependencies) { this.dependencies = dependencies; }

	public void setProject(ProjectNode project2) {
		project=project2;
	}
}