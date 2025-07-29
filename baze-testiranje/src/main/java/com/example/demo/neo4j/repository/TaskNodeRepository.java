package com.example.demo.neo4j.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.example.demo.neo4j.entities.TaskNode;

public interface TaskNodeRepository extends Neo4jRepository<TaskNode, String> {
}
