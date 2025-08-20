package com.example.demo.mongo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.mongo.entities.Role;


public interface RoleRepository extends MongoRepository<Role, String>
{
	Optional<Role> findByName(String name);

	Role findByName(Role roleName);
}
