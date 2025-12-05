package com.sign.signApi.user.service.exceptions;

public class UsernameAlreadyInUseException extends RuntimeException {
    public UsernameAlreadyInUseException(String message) {
        super(message);
    }
}
