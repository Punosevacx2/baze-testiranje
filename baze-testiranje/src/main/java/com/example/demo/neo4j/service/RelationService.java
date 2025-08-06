package com.example.demo.neo4j.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.neo4j.entities.TaskNode;
import com.example.demo.neo4j.entities.UserNode;
import com.example.demo.neo4j.repository.TaskNodeRepository;
import com.example.demo.neo4j.repository.UserNodeRepository;
import com.example.demo.redis.NotificationPublisher;



@Service
public class RelationService {

	private final UserNodeRepository userNodeRepository;
    private final TaskNodeRepository taskNodeRepository;
    private final NotificationPublisher notificationPublisher;

    public RelationService(UserNodeRepository userNodeRepository, TaskNodeRepository taskNodeRepository, NotificationPublisher notificationPublisher) {
        this.userNodeRepository = userNodeRepository;
        this.taskNodeRepository = taskNodeRepository;
        this.notificationPublisher = notificationPublisher;
    }

    @Transactional
    public boolean addCollaborator(String userId, String collaboratorId) {
        Optional<UserNode> userOpt = userNodeRepository.findById(userId);
        Optional<UserNode> collaboratorOpt = userNodeRepository.findById(collaboratorId);

        if (userOpt.isEmpty() || collaboratorOpt.isEmpty()) return false;

        UserNode user = userOpt.get();
        UserNode collaborator = collaboratorOpt.get();

        user.getCollaborators().add(collaborator);
        userNodeRepository.save(user);

     // ➕ Obaveštenje preko Redis-a
        String message = String.format("User %s added collaborator %s", user.getUsername(), collaborator.getUsername());
        notificationPublisher.publish(message);
        
        return true;
    }

    

   
    
        @Transactional
        public boolean addTaskDependency(String taskId, String dependencyId) {
            Optional<TaskNode> taskOpt = taskNodeRepository.findById(taskId);
            Optional<TaskNode> dependencyOpt = taskNodeRepository.findById(dependencyId);

            if (taskOpt.isEmpty() || dependencyOpt.isEmpty()) return false;

            TaskNode task = taskOpt.get();
            TaskNode dependency = dependencyOpt.get();

            task.getDependencies().add(dependency);
            taskNodeRepository.save(task);

            return true;
        }
        
        
    @Transactional
    public boolean setManager(String userId, String managerId) {
        Optional<UserNode> userOpt = userNodeRepository.findById(userId);
        Optional<UserNode> managerOpt = userNodeRepository.findById(managerId);

        if (userOpt.isEmpty() || managerOpt.isEmpty()) return false;

        UserNode user = userOpt.get();
        UserNode manager = managerOpt.get();

        user.setManager(manager);
        userNodeRepository.save(user);

        return true;
    }
}