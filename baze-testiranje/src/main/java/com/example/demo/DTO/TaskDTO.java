package com.example.demo.DTO;

import com.example.demo.mongo.entities.Task;
import com.example.demo.neo4j.entities.TaskNode;

public class TaskDTO {
	private Task task;
    private TaskNode taskNode;

    public TaskDTO(Task task, TaskNode taskNode) {
        this.task = task;
        this.taskNode = taskNode;
    }

    public Task getUser() {
        return task;
    }

    public TaskNode getUserNode() {
        return taskNode;
    }
}
