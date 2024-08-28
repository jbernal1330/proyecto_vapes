package com.project.vapes.token;

import com.project.vapes.exception.TokenNotFoundException;
import com.project.vapes.model.User;

public class GetUserByToken {

    private TokenRepository tokenRepository;

    public User getUser(String token) {
        Token t = tokenRepository.findByToken(token).orElseThrow(() -> new TokenNotFoundException(token));
        User user = t.getUser();
        return user;
    }
}
