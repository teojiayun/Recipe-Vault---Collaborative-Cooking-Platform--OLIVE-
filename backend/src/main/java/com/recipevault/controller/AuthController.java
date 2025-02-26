package com.recipevault.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recipevault.dto.LoginResponseDTO;
import com.recipevault.model.UserInfo;
import com.recipevault.service.UserService;
import com.recipevault.util.JwtUtil;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String fullName = request.get("fullName");
        String password = request.get("password");

        UserInfo newUser = userService.registerUser(username, fullName, password);
        return ResponseEntity.ok(Map.of("message", "User registered successfully", "user", newUser));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        Optional<UserInfo> authenticatedUser = userService.authenticateUser(username, password);
        if (authenticatedUser.isPresent()) {
            UserInfo user = authenticatedUser.get();
            String token = jwtUtil.generateToken(username);

            LoginResponseDTO response = new LoginResponseDTO(
                user.getId(), 
                user.getUsername(), 
                user.getFullName(), 
                token
            );
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }
}
