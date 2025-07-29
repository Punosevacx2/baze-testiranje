package com.example.demo.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.mongo.entities.Task;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByAssignedToUserId(String userId);
    List<Task> findByProjectId(String projectId);
    List<Task> findByStatus(String status);
}