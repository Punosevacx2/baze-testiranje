package com.example.demo.neo4j.repository;

import java.util.Set;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import com.example.demo.neo4j.entities.ProjectNode;
import com.example.demo.neo4j.entities.TaskNode;
import com.example.demo.neo4j.entities.UserNode;

public interface ProjectNodeRepository extends Neo4jRepository<ProjectNode, String> {
	
	@Query("MATCH (u:User)-[:WORKS_ON]->(p:Project {id: $projectId}) RETURN u")
    Set<UserNode> findUsersByProjectId(String projectId);
	
	@Query("MATCH (p:Project {id: $projectId})-[:HAS_MEMBER]->(u:User) RETURN u.id")
    Set<String> findUserIdsByProjectId(String projectId);
	
	@Query("MATCH (p:Project {id: $projectId})-[:HAS_TASK]->(t:Task) RETURN t.id")
    Set<String> findTasksIdsByProjectId(String projectId);
	
}
