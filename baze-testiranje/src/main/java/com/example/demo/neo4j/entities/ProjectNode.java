package com.example.demo.neo4j.entities;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("Project")
public class ProjectNode {

    @Id
    private String id;

    @Relationship(type = "HAS_TASK")
    private Set<TaskNode> tasks = new HashSet<>();

    @Relationship(type = "HAS_MEMBER")
    private Set<UserNode> members = new HashSet<>();

	public ProjectNode() {}

    public ProjectNode(String id) {
        this.id = id;
    }

	public Set<TaskNode> getTasks() {
		
		return tasks;
	}

	public Set<UserNode> getMembers() {
		return members;
	}

	public void setTasks(HashSet hashSet) {
		tasks=hashSet;
		
	}

	public void setMembers(HashSet hashSet) {
		members=hashSet;
	}

    // Constructors, getteri, setteri...
}