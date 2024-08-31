package com.project.vapes.auth;

import com.project.vapes.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

// Entidad en la cual se guardan los datos enviados por el cliente
// Apoya la creación de un nuevo usuario así como su respectivo token
public class RegisterRequest {

    private int id;
    private String fname;
    private String lname;
    private String password;
    private UserRole role;
}
