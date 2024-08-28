package com.project.vapes.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

import static com.project.vapes.model.UserPermission.ADMIN_CREATE;
import static com.project.vapes.model.UserPermission.ADMIN_DELETE;
import static com.project.vapes.model.UserPermission.ADMIN_READ;
import static com.project.vapes.model.UserPermission.ADMIN_UPDATE;
import static com.project.vapes.model.UserRole.ADMIN;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@CrossOrigin(origins = "http://localhost:3000")

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity

// Configuración de seguridad de la App
// Da permisos sobre las URLs permitidas, según el tipo de usuario que las
// utiliza (Admin, manager, normal)
public class SecurityConfiguration {

        // Lista de todas las URLs permitidas en la aplicación
        private static final String[] WHITE_LIST_URL = { "/userAuth/**",
                        "/user", "/user/**", "/userAuth/authenticate", "/users/**",
                        "/users", "/comments", "/comments/**", "/wheels", "/wheels/**",
                        "/logout"
        };
        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        private final LogoutService logoutService;

        @SuppressWarnings("removal")
        @Bean
        // Se encarga de limitar el acceso a determinadas URLs según los permisos del
        // usuario
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // .csrf(AbstractHttpConfigurer::disable)
                                // .cors(cors -> cors.disable())
                                .authorizeHttpRequests(req -> req.requestMatchers(WHITE_LIST_URL)
                                                .permitAll()
                                                .requestMatchers("/admin/**").hasAnyRole(ADMIN.name())
                                                .requestMatchers(GET, "/admin/**")
                                                .hasAnyAuthority(ADMIN_READ.name())
                                                .requestMatchers(POST, "/admin/**")
                                                .hasAnyAuthority(ADMIN_CREATE.name())
                                                .requestMatchers(PUT, "/admin/**")
                                                .hasAnyAuthority(ADMIN_UPDATE.name())
                                                .requestMatchers(DELETE, "/admin/**")
                                                .hasAnyAuthority(ADMIN_DELETE.name())
                                                .anyRequest()
                                                .authenticated())
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                .logout()
                                .logoutUrl("/logout")
                                .addLogoutHandler((request, response, authentication) -> {
                                        logoutService.logout(request, response, authentication);
                                })
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder
                                                .clearContext());
                http.cors().and().csrf().disable();
                return http.build();
        }
}