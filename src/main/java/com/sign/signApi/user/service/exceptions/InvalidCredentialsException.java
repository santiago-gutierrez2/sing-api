package com.sign.signApi.user.service.exceptions;

import com.sign.signApi.common.exceptions.ApiException;

public class InvalidCredentialsException extends ApiException {
    public InvalidCredentialsException() {
        super("Invalid username or password");
    }
}
