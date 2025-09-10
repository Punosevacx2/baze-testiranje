package com.example.demo.neo4j.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.TaskResponseDTO;
import com.example.demo.DTO.UserRespondeDTO;
import com.example.demo.mongo.entities.User;
import com.example.demo.mongo.entities.Task;
import com.example.demo.mongo.service.TaskService;
import com.example.demo.mongo.service.UserService;
import com.example.demo.neo4j.entities.TaskNode;
import com.example.demo.neo4j.entities.UserNode;
import com.example.demo.neo4j.service.ProjectNodeService;
import com.example.demo.neo4j.service.RelationService;

@RestController
@RequestMapping("/relations")
public class RelationController {

    private final RelationService relationService;
    private final ProjectNodeService projectNodeService;
    private final UserService userService;
    private final TaskService taskService;

    public RelationController(RelationService relationService,ProjectNodeService projectNodeService, UserService userService,TaskService taskService) {
        this.relationService = relationService;
        this.projectNodeService=projectNodeService;
        this.userService=userService;
        this.taskService=taskService;
    }

    @PostMapping("/user/{id1}/collaborates/{id2}")
    public ResponseEntity<String> addCollaborator(@PathVariable String id1, @PathVariable String id2) {
        boolean success = relationService.addCollaborator(id1, id2);
        if (success) return ResponseEntity.ok("Collaborator added");
        else return ResponseEntity.badRequest().body("User or collaborator not found");
    }

    @PostMapping("/task/{id1}/depends/{id2}")
    public ResponseEntity<String> addTaskDependency(@PathVariable String id1, @PathVariable String id2) {
        boolean success = relationService.addTaskDependency(id1, id2);
        if (success) return ResponseEntity.ok("Task dependency added");
        else return ResponseEntity.badRequest().body("Task or dependency not found");
    }

    @GetMapping("/projects/{projectId}/users")
    public Set<UserRespondeDTO> getUsersByProject(@PathVariable String projectId) {
        Set<String> userIds = projectNodeService.getUserIdsForProject(projectId);
        System.out.println(userIds);
        Set<User> users = userService.findUsersByIds(userIds);

        return users.stream()
                .map(user -> new UserRespondeDTO(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toSet());
    }
    
    @GetMapping("/{projectId}/tasks")
    public Set<TaskResponseDTO> getTasksByProject(@PathVariable String projectId) {
    	Set<String> taskIds = projectNodeService.getTasksIdForProject(projectId);
        System.out.println(taskIds);
        Set<Task> tasks = taskService.findUsersByIds(taskIds);

        return tasks.stream()
                .map(task -> new TaskResponseDTO(task.getId(), task.getTitle(), task.getDescription(),task.getStatus(),task.getDeadline()))
                .collect(Collectors.toSet());
    }
    
    @PostMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<String> addTaskToProject(
            @PathVariable String projectId,
            @PathVariable String taskId
    ) {
        boolean success = relationService.addTaskToProject(projectId, taskId);

        if (success) {
            return ResponseEntity.ok("Task uspešno dodat u projekat.");
        } else {
            return ResponseEntity.badRequest().body("Project ili Task nije pronađen.");
        }
    }
    
    @PostMapping("/{projectId}/add-user/{userId}")
    public ResponseEntity<String> addUserToProject(@PathVariable String projectId, @PathVariable String userId) {
        boolean success = projectNodeService.addUserToProject(userId, projectId);
        if (success) {
            return ResponseEntity.ok("User added to project");
        } else {
            return ResponseEntity.badRequest().body("User or project not found");
        }
    }
    
//    @PostMapping("/user/{id}/manager/{managerId}")
//    public ResponseEntity<String> setManager(@PathVariable String id, @PathVariable String managerId) {
//        boolean success = relationService.setManager(id, managerId);
//        if (success) return ResponseEntity.ok("Manager set");
//        else return ResponseEntity.badRequest().body("User or manager not found");
//    }
}
