package com.project.vapes.exception;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String token) {
        super("No se encontró usuario con token " + token);
    }
}
