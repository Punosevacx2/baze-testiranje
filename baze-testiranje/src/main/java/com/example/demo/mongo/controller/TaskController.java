package com.example.demo.mongo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.TaskDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.mongo.entities.Task;
import com.example.demo.mongo.entities.User;
import com.example.demo.mongo.service.TaskService;
import com.example.demo.neo4j.entities.TaskNode;
import com.example.demo.neo4j.entities.UserNode;
import com.example.demo.neo4j.service.TaskNodeService;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {
    private final TaskService taskService;
    private final TaskNodeService taskNodeService;

    public TaskController(TaskService taskService,TaskNodeService taskNodeService) {
        this.taskService = taskService;
        this.taskNodeService=taskNodeService;
    }

    @GetMapping
    public List<Task> getAll() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getById(@PathVariable String id) {
    	Optional<Task> mongoTaskOpt = taskService.getTaskById(id);
        Optional<TaskNode> neo4jTaskOpt = taskNodeService.getTaskById(id);

        if (mongoTaskOpt.isEmpty() || neo4jTaskOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        TaskDTO dto = new TaskDTO(mongoTaskOpt.get(), neo4jTaskOpt.get());
        return ResponseEntity.ok(dto);
        }

    @PostMapping
    public Task create(@RequestBody Task task) {
    	Task savedTask = taskService.createTask(task);

        TaskNode taskNode = new TaskNode(savedTask.getId());
        taskNodeService.createTask(taskNode);

        return savedTask;
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable String id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        taskService.deleteTask(id);
        taskNodeService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
