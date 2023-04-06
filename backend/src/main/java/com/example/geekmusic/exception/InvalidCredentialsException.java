package com.example.geekmusic.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidCredentialsException  extends AuthenticationException {

    private String message;
    public InvalidCredentialsException(String message) {
        super(message);
        this.message = message;
    }

}
