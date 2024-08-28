package com.project.vapes.repository;

import com.project.vapes.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    long count();

    // Check if a user with the given email exists
    boolean existsByEmail(String email);

    // Check if a user with the given username exists
    boolean existsByUsername(String username);

    // Retorna el usuario si se encuentra en el repositorio
    Optional<User> findByEmail(String email);

    User findByUsername(String username);

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN FETCH u.tokens t " +
            "WHERE t.id = (SELECT MAX(t2.id) FROM Token t2 " +
            "WHERE t2.user = u AND t2.expired = false)")
    List<User> findAllWithValidToken();

}