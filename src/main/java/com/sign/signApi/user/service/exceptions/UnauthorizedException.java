package com.sign.signApi.user.service.exceptions;

import com.sign.signApi.common.exceptions.ApiException;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
