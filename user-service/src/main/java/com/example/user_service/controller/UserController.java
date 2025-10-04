package com.example.user_service.controller;

import com.example.user_service.dto.LoginRequest;
import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Register first, or redirect to login if already exists
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest loginRequest) {
        Optional<User> existingUser = userRepository.findByUsername(loginRequest.getUsername());

        if (existingUser.isPresent()) {
            return ResponseEntity.ok("{\"message\": \"User already registered, please login.\"}");
        }

        User newUser = new User();
        newUser.setUsername(loginRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(loginRequest.getPassword()));

        userRepository.save(newUser);

        return ResponseEntity.ok("{\"message\": \"User registered successfully. Please login now.\"}");
    }

    // ✅ Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());

        if (userOpt.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), userOpt.get().getPassword())) {
            String token = jwtService.generateToken(loginRequest.getUsername());
            return ResponseEntity.ok("{\"token\": \"" + token + "\"}");
        }

        return ResponseEntity.status(401).body("{\"error\": \"Invalid credentials\"}");
    }
}
