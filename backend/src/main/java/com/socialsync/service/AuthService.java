package com.socialsync.service;

import com.socialsync.dto.AuthRequest;
import com.socialsync.dto.AuthResponse;
import com.socialsync.dto.RegisterRequest;
import com.socialsync.model.User;
import com.socialsync.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void seedDemoUser() {
        if (!userRepository.existsByEmail("demo@socialsync.io")) {
            User demo = new User(
                    "Sarah Jenkins",
                    "demo@socialsync.io",
                    "password123",
                    "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150"
            );
            userRepository.save(demo);
        }
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        User user = new User(
                request.getFullName().trim(),
                request.getEmail().trim().toLowerCase(),
                request.getPassword().trim(),
                "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150"
        );

        User saved = userRepository.save(user);
        String token = "usr_" + saved.getId() + "_" + UUID.randomUUID().toString();

        return new AuthResponse(
                token,
                saved.getId(),
                saved.getFullName(),
                saved.getEmail(),
                saved.getAvatarUrl()
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        if (!user.getPassword().equals(request.getPassword().trim())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        String token = "usr_" + user.getId() + "_" + UUID.randomUUID().toString();

        return new AuthResponse(
                token,
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getAvatarUrl()
        );
    }
}
