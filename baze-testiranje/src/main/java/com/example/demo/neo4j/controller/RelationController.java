package com.example.demo.neo4j.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.neo4j.service.RelationService;

@RestController
@RequestMapping("/relations")
public class RelationController {

    private final RelationService relationService;

    public RelationController(RelationService relationService) {
        this.relationService = relationService;
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

//    @PostMapping("/user/{id}/manager/{managerId}")
//    public ResponseEntity<String> setManager(@PathVariable String id, @PathVariable String managerId) {
//        boolean success = relationService.setManager(id, managerId);
//        if (success) return ResponseEntity.ok("Manager set");
//        else return ResponseEntity.badRequest().body("User or manager not found");
//    }
}
