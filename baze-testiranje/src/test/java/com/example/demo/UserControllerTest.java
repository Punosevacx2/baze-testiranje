package com.example.demo;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.mongo.controller.UserController;
import com.example.demo.mongo.entities.User;
import com.example.demo.mongo.service.UserService;
import com.example.demo.neo4j.entities.UserNode;
import com.example.demo.neo4j.service.UserNodeService;

//@SpringBootTest
//@AutoConfigureMockMvc
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserNodeService userNodeService;

    @InjectMocks
    private UserController userController;

    private User user;
    private UserNode userNode;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user = new User();
        user.setId("68a735568ecec5a784b925e0");
        user.setUsername("manager1");
        user.setEmail("manager1@manager.com.com");

        userNode = new UserNode("68a735568ecec5a784b925e0");
    }

    //@Test
    @WithMockUser
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("68a735568ecec5a784b925e0"))
                .andExpect(jsonPath("$[0].username").value("milos"));
    }

    //@Test
    @WithMockUser
    void testGetByIdFound() throws Exception {
        when(userService.getUserById("68a735568ecec5a784b925e0")).thenReturn(Optional.of(user));
        when(userNodeService.getUserById("68a735568ecec5a784b925e0")).thenReturn(Optional.of(userNode));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/users/68a735568ecec5a784b925e0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("manager1"))
                .andExpect(jsonPath("$.id").value("68a735568ecec5a784b925e0"));
    }

    //@Test
    @WithMockUser
    void testGetByIdNotFound() throws Exception {
        when(userService.getUserById("68a735568ecec5a784b925e0")).thenReturn(Optional.empty());

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/users/68a735568ecec5a784b925e0"))
                .andExpect(status().isNotFound());
    }

    //@Test
    @WithMockUser(username = "manager1", roles = {"MANAGER"})
    void testAuthenticatedUser() throws Exception {
        when(userService.getUserById("68a735568ecec5a784b925e0")).thenReturn(Optional.of(user));
        when(userNodeService.getUserById("68a735568ecec5a784b925e0")).thenReturn(Optional.of(userNode));

        // simulacija SecurityContext-a
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(user, null, List.of())
        );

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("68a735568ecec5a784b925e0"))
                .andExpect(jsonPath("$.username").value("manager"));
    }
}
