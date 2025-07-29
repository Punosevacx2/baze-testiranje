package com.example.demo.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.mongo.entities.Project;

public interface ProjectRepository extends MongoRepository<Project, String> {
    List<Project> findByMemberIdsContaining(String userId);
}
