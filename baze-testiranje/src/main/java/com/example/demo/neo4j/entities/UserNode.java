package com.example.demo.neo4j.entities;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("User")
public class UserNode {

    @Id
    private String id;

    @Relationship(type = "COLLABORATES_WITH")
    private Set<UserNode> collaborators = new HashSet<>();

    
    @Relationship(type = "WORKS_ON")
    private Set<ProjectNode> projects = new HashSet<>();
    // Constructors, getters, setters

    public UserNode() {}

    public UserNode(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Set<UserNode> getCollaborators() { return collaborators; }
    public void setCollaborators(Set<UserNode> collaborators) { this.collaborators = collaborators; }

    
	public Object getUsername() {
		return id;
	}

	public Set<ProjectNode> getProjects() {
		
		return projects;
	}

	
}
