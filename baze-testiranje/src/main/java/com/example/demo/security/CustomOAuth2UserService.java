package com.example.demo.security;



import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.mongo.entities.Role;
import com.example.demo.mongo.entities.User;
import com.example.demo.mongo.repository.RoleRepository;
import com.example.demo.mongo.repository.UserRepository;
import com.example.demo.security.*;

import lombok.RequiredArgsConstructor;

//@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private  UserRepository userRepository;
    private  PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauth2User = super.loadUser(userRequest);
        CustomOAuth2User customUser = new CustomOAuth2User(oauth2User);

        String email = customUser.getEmail();
        String username = customUser.getUsername();

        if (email == null) {
            throw new RuntimeException("Email not found from OAuth2 provider");
        }

        Optional<User> optionalUser = userRepository.findById(email);

        if (optionalUser.isEmpty()) {
            // Korisnik ne postoji -> kreiraj ga
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(username != null ? username : email);
            newUser.setFirstName(customUser.getFirstName() != null ? customUser.getFirstName() : customUser.getFullName());
            newUser.setLastName(customUser.getLastName() != null ? customUser.getLastName() : "");
            newUser.setCountry("Not set");
            newUser.setCity("Not set");
            newUser.setPostalCode("00000");

            // Postavi dummy password jer OAuth ne koristi lozinku
            newUser.setPassword(passwordEncoder.encode("oauth2user"));

            // Postavi default rolu
            Role userRole = roleRepository.findByName("ROLE_USER");
          //  newUser.setRoles(userRole);

            userRepository.save(newUser);
        }

        return customUser;
    }
}