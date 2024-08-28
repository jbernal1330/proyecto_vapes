package com.project.vapes.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

// Creación de solicitudes de autentificación (se realiza con los datos de email
// y contraseña del usuario --> login)
public class AuthenticationRequest {

  private String email;
  private String password;
}
