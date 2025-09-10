package com.example.demo.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.mongo.entities.ProjectMessage;

public interface ChatMessageRepository extends MongoRepository<ProjectMessage, String> {
	List<ProjectMessage> findByProjectIdOrderByTimestampAsc(String projectId);
	  List<ProjectMessage> findByProjectId(String projectId);
}
