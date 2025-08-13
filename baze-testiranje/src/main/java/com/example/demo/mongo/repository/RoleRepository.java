package com.example.demo.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.mongo.entities.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByName(String name);
}