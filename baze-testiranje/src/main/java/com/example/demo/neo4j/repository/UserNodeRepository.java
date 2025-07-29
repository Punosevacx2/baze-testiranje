package com.example.demo.neo4j.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.example.demo.neo4j.entities.UserNode;

public interface UserNodeRepository extends Neo4jRepository<UserNode, String> {
}
