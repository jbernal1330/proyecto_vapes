package com.project.vapes.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Integer id) {
        super("No se encontró usuario con id " + id);
    }
}
