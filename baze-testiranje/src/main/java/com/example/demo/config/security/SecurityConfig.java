package com.example.demo.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.demo.security.CustomOAuth2UserService;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	//@Autowired
	//private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;
	@Bean
	    public SecurityFilterChain filterChain(HttpSecurity http,  CorsConfigurationSource corsConfigurationSource) throws Exception {
	         http.cors(cors -> cors.configurationSource(corsConfigurationSource)) // Use the CORS configuration
             .csrf((csrf) -> csrf.disable())
	           
	            .authorizeHttpRequests(auth -> auth
	            	.requestMatchers("/admin/**").hasRole("ADMIN")
	                .requestMatchers("/register", "/login", "/css/**","/js/**","/change-language","/ws/**", "/topic/**").permitAll()
	                .anyRequest().authenticated()
	            )
	            .oauth2Login(oauth2 -> oauth2
	                    .userInfoEndpoint(userInfo -> userInfo
	                        .userService(customOAuth2UserService) // mapiranje OAuth2 korisnika
	                    )
	                    .defaultSuccessUrl("/home", true)
	                )
	                
	                // Form login sa default login page
	                .formLogin(form -> form
	                    .defaultSuccessUrl("/home", true)
	                );
	        // http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

	         
	        return http.build();
	    }
}