package com.project.vapes.auth;

import com.project.vapes.config.JwtService;
import com.project.vapes.model.User;
import com.project.vapes.repository.UserRepository;
import com.project.vapes.token.Token;
import com.project.vapes.token.TokenRepository;
import com.project.vapes.token.TokenType;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Realiza y administra procesos de registro y autentificación para los usuarios (register y login)
@Service
@RequiredArgsConstructor
public class AuthenticationService {
        private final UserRepository repository;
        private final TokenRepository tokenRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        // Registro de usuarios --> Se crea el usuario y se genera el token
        public AuthenticationResponse register(RegisterRequest request) {
                var user = User.builder()
                                .username(request.getUsername())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(request.getRole())
                                .build();
                repository.save(user);
                var savedUser = repository.save(user);
                var jwtToken = jwtService.generateToken(user);
                var refreshToken = jwtService.generateRefreshToken(user);
                saveUsertoken(savedUser, jwtToken);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .refreshToken(refreshToken)
                                .build();
        }

        // Proceso de autentificación para usuarios que desean iniciar sesi
        // Compara los datos y la integridad de los mismos por medio de JWT
        // Permite iniciar la sesión respectiv
        // corresponden a un usuario ya creado
        // Retorna una respuesta de autentificación, generada en base a tokens
        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getUsername(),
                                                request.getPassword()));
                var user = repository.findByUsername(request.getUsername()).orElseThrow();

                var jwtToken = jwtService.generateToken(user);
                var refreshToken = jwtService.generateRefreshToken(user);
                revokeAllUserTokens(user);
                saveUsertoken(user, jwtToken);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .refreshToken(refreshToken)
                                .role(user.getRole())
                                .build();
        }

        private void revokeAllUserTokens(User user) {
                var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
                if (validUserTokens.isEmpty())
                        return;
                validUserTokens.forEach(t -> {
                        t.setExpired(true);
                        t.setRevoked(true);
                });
                tokenRepository.saveAll(validUserTokens);
        }

        private void saveUsertoken(User user, String jwtToken) {
                var token = Token.builder()
                                .user(user)
                                .token(jwtToken)
                                .tokenType(TokenType.BEARER)
                                .revoked(false)
                                .expired(false)
                                .build();
                tokenRepository.save(token);
        }
}
