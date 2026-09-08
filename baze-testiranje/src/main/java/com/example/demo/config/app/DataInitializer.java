package com.example.demo.config.app;

import com.example.demo.mongo.entities.Role;
import com.example.demo.mongo.entities.User;
import com.example.demo.mongo.repository.RoleRepository;
import com.example.demo.mongo.repository.UserRepository;
import com.example.demo.neo4j.entities.UserNode;
import com.example.demo.neo4j.service.UserNodeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserNodeService userNodeService;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UserNodeService userNodeService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userNodeService = userNodeService;
    }

    @Override
    public void run(String... args) {
        // Inicijalizacija rola
        List<String> roles = List.of("USER", "ADMIN", "MANAGER");
        for (String roleName : roles) {
            if (roleRepository.findByName(roleName) == null) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
                System.out.println("Kreirana rola: " + roleName);
            }
        }

        // Inicijalizacija default admin naloga
        if (userRepository.findByEmail("admin@admin.rs").isEmpty()) {
            Role adminRole = roleRepository.findByName("ADMIN");

            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@admin.rs");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(adminRole);

            User savedAdmin = userRepository.save(admin);

            UserNode adminNode = new UserNode(savedAdmin.getId());
            userNodeService.createUserNode(adminNode);

            System.out.println("Kreiran default admin nalog: admin@admin.rs");
        }
    }
}
