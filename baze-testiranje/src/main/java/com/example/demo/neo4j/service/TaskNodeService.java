package com.example.demo.neo4j.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.neo4j.entities.TaskNode;
import com.example.demo.neo4j.repository.TaskNodeRepository;

@Service
public class TaskNodeService {
	private final TaskNodeRepository taskNodeRepository;

    public TaskNodeService(TaskNodeRepository taskNodeRepository) {
        this.taskNodeRepository = taskNodeRepository;
    }

    public TaskNode createTask(TaskNode taskNode) {
        return taskNodeRepository.save(taskNode);
    }

    public Optional<TaskNode> getTaskById(String id) {
        return taskNodeRepository.findById(id);
    }

    public List<TaskNode> getAllTasks() {
        return taskNodeRepository.findAll();
    }

    public TaskNode updateTask(TaskNode taskNode) {
        return taskNodeRepository.save(taskNode);
    }

    public void deleteTask(String id) {
        taskNodeRepository.deleteById(id);
    }
}
