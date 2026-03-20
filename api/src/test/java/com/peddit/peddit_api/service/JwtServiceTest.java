package com.peddit.peddit_api.service;

import com.peddit.peddit_api.config.JwtProperties;
import com.peddit.peddit_api.entity.User;
import com.peddit.peddit_api.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JwtServiceTest {

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private JwtService jwtService;

    private User user;

    @BeforeEach
    void setUp() {
        when(jwtProperties.getSecret())
                .thenReturn("test-secret-key-must-be-at-least-256-bits-long-for-hmac");
        when(jwtProperties.getExpirationMs()).thenReturn(86400000L);

        user = User.builder()
                .id(1L)
                .username("pedrokeita")
                .email("pedro@email.com")
                .role(UserRole.USER)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Deve gerar token JWT válido")
    void generateToken_success() {
        String token = jwtService.generateToken(user);

        assertThat(token).isNotNull();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("Deve extrair email do token corretamente")
    void extractEmail_success() {
        String token = jwtService.generateToken(user);
        String email = jwtService.extractEmail(token);

        assertThat(email).isEqualTo("pedro@email.com");
    }

    @Test
    @DisplayName("Deve validar token válido como true")
    void isTokenValid_validToken() {
        String token = jwtService.generateToken(user);

        assertThat(jwtService.isTokenValid(token)).isTrue();
    }

    @Test
    @DisplayName("Deve validar token inválido como false")
    void isTokenValid_invalidToken() {
        assertThat(jwtService.isTokenValid("token.invalido.aqui")).isFalse();
    }

    @Test
    @DisplayName("Deve extrair claims com userId, username e role")
    void extractClaims_containsCorrectData() {
        String token = jwtService.generateToken(user);
        var claims = jwtService.extractClaims(token);

        assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
        assertThat(claims.get("username", String.class)).isEqualTo("pedrokeita");
        assertThat(claims.get("role", String.class)).isEqualTo("USER");
    }
}