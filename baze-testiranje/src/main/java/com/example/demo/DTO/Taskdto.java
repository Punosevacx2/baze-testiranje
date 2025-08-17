package com.example.demo.DTO;

import com.example.demo.mongo.entities.Task;
import com.example.demo.neo4j.entities.TaskNode;

public class Taskdto {
	private Task task;
    private TaskNode taskNode;

    public Taskdto(Task task, TaskNode taskNode) {
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
