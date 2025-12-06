package com.sign.signApi.user.service.exceptions;

import com.sign.signApi.common.exceptions.ApiException;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
