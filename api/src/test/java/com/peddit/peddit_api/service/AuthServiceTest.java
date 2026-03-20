package com.peddit.peddit_api.service;

import com.peddit.peddit_api.dto.request.LoginRequest;
import com.peddit.peddit_api.dto.request.RegisterRequest;
import com.peddit.peddit_api.dto.response.AuthResponse;
import com.peddit.peddit_api.dto.response.UserResponse;
import com.peddit.peddit_api.entity.User;
import com.peddit.peddit_api.entity.UserRole;
import com.peddit.peddit_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("pedrokeita");
        registerRequest.setEmail("pedro@email.com");
        registerRequest.setPassword("123456");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("pedro@email.com");
        loginRequest.setPassword("123456");

        user = User.builder()
                .id(1L)
                .username("pedrokeita")
                .email("pedro@email.com")
                .passwordHash("hashed_password")
                .role(UserRole.USER)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso")
    void register_success() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hashed_password");
        when(userRepository.save(any())).thenReturn(user);

        UserResponse response = authService.register(registerRequest);

        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo("pedrokeita");
        assertThat(response.getEmail()).isEqualTo("pedro@email.com");
        assertThat(response.getRole()).isEqualTo("USER");
        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já cadastrado")
    void register_emailAlreadyExists() {
        when(userRepository.existsByEmail(any())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email já cadastrado");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando username já cadastrado")
    void register_usernameAlreadyExists() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByUsername(any())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username já cadastrado");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve fazer login com sucesso e retornar token JWT")
    void login_success() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtService.generateToken(any())).thenReturn("mocked.jwt.token");
        when(jwtService.getExpirationMs()).thenReturn(86400000L);

        AuthResponse response = authService.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mocked.jwt.token");
        assertThat(response.getType()).isEqualTo("Bearer");
        assertThat(response.getUsername()).isEqualTo("pedrokeita");
        assertThat(response.getRole()).isEqualTo("USER");
    }

    @Test
    @DisplayName("Deve lançar exceção quando email não encontrado")
    void login_emailNotFound() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Credenciais inválidas");
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha incorreta")
    void login_wrongPassword() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Credenciais inválidas");
    }

    @Test
    @DisplayName("Deve lançar exceção quando conta está desativada")
    void login_inactiveAccount() {
        user.setActive(false);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Conta desativada");
    }
}