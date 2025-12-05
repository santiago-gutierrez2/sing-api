package com.sign.signApi.user.service.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Invalid username or password");
    }
}
