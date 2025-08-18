package com.example.demo.mongo.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.LoginResponseDTO;
import com.example.demo.DTO.RequestUserDTO;
import com.example.demo.DTO.ResponseUserDTO;
import com.example.demo.DTO.Userlogindto;
import com.example.demo.mongo.entities.Role;
import com.example.demo.mongo.entities.User;
import com.example.demo.mongo.repository.RoleRepository;
import com.example.demo.mongo.repository.UserRepository;
import com.example.demo.mongo.service.AuthenticationService;
import com.example.demo.mongo.service.JwtService;
import com.example.demo.neo4j.entities.UserNode;
import com.example.demo.neo4j.service.UserNodeService;

import java.util.Map;

@RequestMapping("/auth")
@RestController
@CrossOrigin(origins ={"http://localhost:3000"})
@Tag(name = "User management, login and logout", description = "Operations related to user lifecycle, login and logout")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserNodeService userNodeService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    private LogoutController logoutController;

    public AuthenticationController(JwtService jwtService, 
    								AuthenticationService authenticationService, 
    								UserNodeService userNodeService,
    								UserRepository userRepository, 	
    								PasswordEncoder passwordEncoder,
    								RoleRepository roleRepository)
    		 {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.logoutController = logoutController;
        this.userNodeService=userNodeService;
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.roleRepository=roleRepository;
    }

    @Operation(summary = "Sign up for the account", description = "Create user account")
    @PostMapping("/signup")
    public ResponseEntity<ResponseUserDTO> register(@Valid @RequestBody RequestUserDTO requestUserDto) {
        
        // 1. Kreiranje i popunjavanje User entiteta
        User user = new User();
        user.setUsername(requestUserDto.getUsername());
        user.setEmail(requestUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestUserDto.getPassword()));

        // Postavljanje role (pretpostavljamo da postoji role "USER")
        Role role = roleRepository.findByName("USER");
        if(role == null){
            throw new RuntimeException("Default role USER not found");
        }
        user.setRoles(role);

        // 2. Čuvanje korisnika u MongoDB
        User savedUser = userRepository.save(user);

        // 3. Kreiranje UserNode u Neo4j
        UserNode userNode = new UserNode(savedUser.getId());
        userNodeService.createUserNode(userNode);

        // 4. Priprema Response DTO
        ResponseUserDTO responseUserDTO = new ResponseUserDTO();
        responseUserDTO.setId(savedUser.getId());
        responseUserDTO.setUsername(savedUser.getUsername());
        responseUserDTO.setEmail(savedUser.getEmail());
        responseUserDTO.setRole(savedUser.getRoles());
        responseUserDTO.setPasswort(savedUser.getPassword());

        // 5. Logovanje (opciono)
        System.out.println(responseUserDTO.toString());

        // 6. Vraćanje odgovora sa HTTP 201 CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUserDTO);
    }


    @Operation(summary = "Login to the application", description = "Login with the valid user credentials and obtain JWT token")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@Valid @RequestBody Userlogindto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        Map<String, String> tokens = jwtService.generateTokens(authenticatedUser);
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(tokens.get("accessToken"), tokens.get("refreshToken"), jwtService.getExpirationTime());

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", tokens.get("accessToken"))
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 15) // 15 minutes
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.get("refreshToken"))
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24 * 30) // 30 days
                .build();

        return ResponseEntity
                .ok()
                .header("Set-Cookie", accessTokenCookie.toString())
                .header("Set-Cookie", refreshTokenCookie.toString())
                .body(loginResponseDTO);
    }

//    @Operation(summary = "Token refresh", description = "Refresh the JWT token")
//    @PostMapping("/refresh")
//    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request) {
//        String refreshToken = request.get("refreshToken");
//        Map<String, String> tokens = jwtService.refreshTokens(refreshToken);
//        return ResponseEntity.ok(tokens);
//    }

    @Operation(summary = "Token refresh", description = "Refresh the JWT token")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if (jwtService.validateToken(token)) {
                Map<String, String> tokens = jwtService.refreshTokens(token);

                // Optionally, generate a new refresh token if necessary
                ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", tokens.get("accessToken"))
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(60 * 15) // 15 minutes
                        .build();

                ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.get("refreshToken"))
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(60 * 60 * 24 * 30) // 30 days
                        .build();

                return ResponseEntity
                        .ok()
                        .header("Set-Cookie", accessTokenCookie.toString())
                        .header("Set-Cookie", refreshTokenCookie.toString())
                        .body(tokens);
            }
        }
        return ResponseEntity.badRequest().body("Invalid refresh token");
    }
}