package com.example.demo.neo4j.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.mongo.entities.Notification;
import com.example.demo.neo4j.entities.ProjectNode;
import com.example.demo.neo4j.entities.TaskNode;
import com.example.demo.neo4j.entities.UserNode;
import com.example.demo.neo4j.repository.ProjectNodeRepository;
import com.example.demo.neo4j.repository.TaskNodeRepository;
import com.example.demo.neo4j.repository.UserNodeRepository;
import com.example.demo.redis.NotificationPublisher;



@Service
public class RelationService {

	private final UserNodeRepository userNodeRepository;
    private final TaskNodeRepository taskNodeRepository;
    private final NotificationPublisher notificationPublisher;
    private final ProjectNodeRepository projectNodeRepository;

    public RelationService(UserNodeRepository userNodeRepository, TaskNodeRepository taskNodeRepository, NotificationPublisher notificationPublisher,ProjectNodeRepository projectNodeRepository) {
        this.userNodeRepository = userNodeRepository;
        this.taskNodeRepository = taskNodeRepository;
        this.notificationPublisher = notificationPublisher;
        this.projectNodeRepository=projectNodeRepository;
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

        // ➕ Obaveštenje preko Redis-a (koristi Notification objekat)
        Notification notif = new Notification(
            collaborator.getId(),                  // kome ide
            "COLLABORATOR_ADDED",                   // tip
            String.format("Dodati ste kao saradnik korisniku %s", user.getUsername().toString())                          // ID korisnika kome je dodat
        );

        notificationPublisher.publish("Notifikation",notif); // koristi novu publish funkciju

        return true;
    }


    @Transactional
    public boolean addTaskToProject(String projectId, String taskId) {
        Optional<ProjectNode> projectOpt = projectNodeRepository.findById(projectId);
        Optional<TaskNode> taskOpt = taskNodeRepository.findById(taskId);

        if (projectOpt.isEmpty() || taskOpt.isEmpty()) return false;

        ProjectNode project = projectOpt.get();
        TaskNode task = taskOpt.get();

        project.getTasks().add(task);
        task.setProject(project);

        projectNodeRepository.save(project);
        taskNodeRepository.save(task);

        return true;
    }

    @Transactional
    public boolean addUserToProject(String userId, String projectId) {
        Optional<UserNode> userOpt = userNodeRepository.findById(userId);
        Optional<ProjectNode> projectOpt = projectNodeRepository.findById(projectId);

        if (userOpt.isEmpty() || projectOpt.isEmpty()) return false;

        UserNode user = userOpt.get();
        ProjectNode project = projectOpt.get();

        user.getProjects().add(project);
        project.getMembers().add(user);

        userNodeRepository.save(user);
        projectNodeRepository.save(project);

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