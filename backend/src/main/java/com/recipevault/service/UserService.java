package com.recipevault.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import com.recipevault.model.UserInfo;
import com.recipevault.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UserInfo registerUser(String username, String fullName, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        UserInfo user = new UserInfo();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setPassword(passwordEncoder.encode(password)); // ✅ Hash password before saving
        return userRepository.save(user);
    }

    public Optional<UserInfo> authenticateUser(String username, String password) {
        Optional<UserInfo> user = userRepository.findByUsername(username);
        return user.filter(u -> passwordEncoder.matches(password, u.getPassword()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return User.builder() // ✅ Using Spring Security's User class
                   .username(user.get().getUsername())
                   .password(user.get().getPassword())
                   .roles("USER") // ✅ Assign role (optional)
                   .build();
    }
}