package com.example.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private OAuth2User oauth2User;

    public CustomOAuth2User(OAuth2User oauth2User) {
        this.oauth2User = oauth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getName();
    }

    // ✨ CUSTOM METODE koje ti trebaju:

    public String getEmail() {
        // Google koristi "email", GitHub koristi "email" (ali može biti null)
        return (String) oauth2User.getAttribute("email");
    }

    public String getUsername() {
        // GitHub koristi "login", Google koristi "email" ili "name"
        String login = (String) oauth2User.getAttribute("login"); // GitHub
        if (login != null) {
            return login;
        }
        return (String) oauth2User.getAttribute("email"); // Google
    }

    public String getFullName() {
        // Google koristi "name", GitHub koristi "name"
        return (String) oauth2User.getAttribute("name");
    }

    public String getFirstName() {
        return (String) oauth2User.getAttribute("given_name"); // Google only
    }

    public String getLastName() {
        return (String) oauth2User.getAttribute("family_name"); // Google only
    }

    public String getProfilePicture() {
        // Google koristi "picture", GitHub koristi "avatar_url"
        String picture = (String) oauth2User.getAttribute("picture"); // Google
        if (picture == null) {
            picture = (String) oauth2User.getAttribute("avatar_url"); // GitHub
        }
        return picture;
    }
}
