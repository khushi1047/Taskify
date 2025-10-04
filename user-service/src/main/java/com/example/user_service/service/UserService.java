package com.example.user_service.service;

import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User register(String username, String password) {
        if(userRepository.findByUsername(username).isPresent()){
            throw new RuntimeException("User already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if(passwordEncoder.matches(password, user.getPassword())){
            return jwtService.generateToken(username);
        }
        throw new RuntimeException("Invalid credentials");
    }
}
