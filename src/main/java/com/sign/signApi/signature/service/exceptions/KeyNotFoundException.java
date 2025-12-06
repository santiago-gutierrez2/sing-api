package com.sign.signApi.signature.service.exceptions;

import com.sign.signApi.common.exceptions.ApiException;

public class KeyNotFoundException extends ApiException {
    public KeyNotFoundException(String message) {
        super(message);
    }
}
