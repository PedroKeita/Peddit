package com.peddit.peddit_api.service;

import com.peddit.peddit_api.dto.request.RegisterRequest;
import com.peddit.peddit_api.dto.response.UserResponse;
import com.peddit.peddit_api.entity.User;
import com.peddit.peddit_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username já cadastrado");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();

        User saved = userRepository.save(user);
        return UserResponse.from(saved);
    }
}
