package com.example.demo.neo4j.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.demo.neo4j.entities.ProjectNode;
import com.example.demo.neo4j.entities.TaskNode;
import com.example.demo.neo4j.entities.UserNode;
import com.example.demo.neo4j.repository.ProjectNodeRepository;
import com.example.demo.neo4j.repository.UserNodeRepository;

@Service
public class ProjectNodeService {
	private final ProjectNodeRepository projectNodeRepository;
	private final UserNodeRepository userNodeRepository;

    public ProjectNodeService(ProjectNodeRepository projectNodeRepository,
    		UserNodeRepository userNodeRepository) {
        this.projectNodeRepository = projectNodeRepository;
        this.userNodeRepository=userNodeRepository;
        
    }
	
	public ProjectNode createProject(ProjectNode project) {
        if (project.getTasks() == null) {
            project.setTasks(new HashSet<>());
        }
        if (project.getMembers() == null) {
            project.setMembers(new HashSet<>());
        }
        return projectNodeRepository.save(project);
    }

    public Optional<ProjectNode> getProjectById(String id) {
        return projectNodeRepository.findById(id);
    }

    public Iterable<ProjectNode> getAllProjects() {
        return projectNodeRepository.findAll();
    }

    public void deleteProject(String id) {
        projectNodeRepository.deleteById(id);
    }
    public Set<UserNode> getUsersForProject(String projectId) {
        return projectNodeRepository.findUsersByProjectId(projectId);
    }
    public Set<String> getTasksIdForProject(String projectId) {
        return projectNodeRepository.findTasksIdsByProjectId(projectId);
    }
    
    public boolean addUserToProject(String userId, String projectId) {
        Optional<UserNode> userOpt = userNodeRepository.findById(userId);
        Optional<ProjectNode> projectOpt = projectNodeRepository.findById(projectId);

        if (userOpt.isPresent() && projectOpt.isPresent()) {
            UserNode user = userOpt.get();
            ProjectNode project = projectOpt.get();

            // Kreiranje relacije
            user.getProjects().add(project);
            project.getMembers().add(user);

            userNodeRepository.save(user);
            projectNodeRepository.save(project);

            return true;
        }
        return false;
    }

    
    public Set<String> getUserIdsForProject(String projectId) {
        return projectNodeRepository.findUserIdsByProjectId(projectId);
    }

//    public Set<UserNode> getUsersForProject(String projectId) {
//        return projectNodeRepository.findById(projectId)
//                .map(ProjectNode::getUsers)
//                .orElse(Collections.emptySet());
//    }

}
