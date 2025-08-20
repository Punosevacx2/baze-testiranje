package com.example.demo.config.security;

import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.mongo.repository.UserRepository;
import com.example.demo.mongo.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig
{

	private final JwtService jwtService;
	private final UserRepository userRepository;

	public SecurityConfig(JwtService jwtService, UserRepository userRepository)
	{
		this.jwtService = jwtService;
		this.userRepository = userRepository;
	}

	// CORS: dozvoli localhost front (prilagodi po potrebi)
	@Bean
	UrlBasedCorsConfigurationSource corsConfigurationSource()
	{
		CorsConfiguration cfg = new CorsConfiguration();
		cfg.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:5173"));
		cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		cfg.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
		cfg.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", cfg);
		return source;
	}

	@Bean
	OncePerRequestFilter jwtFilter()
	{
		return new OncePerRequestFilter()
		{
			@Override
			protected boolean shouldNotFilter(HttpServletRequest req)
			{
				String p = req.getRequestURI();
				return p.startsWith("/auth/") || p.startsWith("/swagger-ui") || p.startsWith("/v3/api-docs") || p.equals("/")
						|| p.startsWith("/actuator") || p.startsWith("/ping")          // naš test endpoint
						|| "OPTIONS".equalsIgnoreCase(req.getMethod()); // CORS preflight
			}

			@Override
			protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
					throws IOException, jakarta.servlet.ServletException
			{

				String auth = req.getHeader("Authorization");
				if (auth != null && auth.startsWith("Bearer "))
				{
					String token = auth.substring(7);
					try
					{
						String email = jwtService.extractEmail(token);
						if (email != null && SecurityContextHolder.getContext().getAuthentication() == null)
						{
							var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
							if (jwtService.isTokenValid(token, user))
							{
								var authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
								SecurityContextHolder.getContext().setAuthentication(authToken);
							}
						}
					}
					catch (Exception ignored)
					{
						// ako je token loš, pusti dalje -> završiće kao 401/403 na aut rules
					}
				}
				chain.doFilter(req, res);
			}
		};
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, UrlBasedCorsConfigurationSource cors) throws Exception
	{
		http.csrf(csrf -> csrf.disable()).cors(c -> c.configurationSource(cors))
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(
						auth -> auth.requestMatchers(
										"/",
										"/ping",
										"/auth/**",
										"/swagger-ui/**",     // assets
										"/swagger-ui.html",   // UI stranica (custom path)
										"/api-docs/**",       // OpenAPI JSON (custom path)
										"/actuator/**"
								)
								.permitAll()
								.requestMatchers(HttpMethod.GET, "/public/**").permitAll()
								.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
								.anyRequest()
								.authenticated())
				.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
