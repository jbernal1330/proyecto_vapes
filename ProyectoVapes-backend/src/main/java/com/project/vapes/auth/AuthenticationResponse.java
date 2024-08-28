package com.project.vapes.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.vapes.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

// Respuesta a las solicitudes de autentificación realizadas
// (AuthenticationRequest's)
public class AuthenticationResponse {

  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("refresh_token")
  private String refreshToken;
  private UserRole role;
}