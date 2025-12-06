package com.sign.signApi.signature.service.exceptions;

import com.sign.signApi.common.exceptions.ApiException;

public class KeyGenerationException extends ApiException {
    public KeyGenerationException(String message) {
        super(message);
    }
}
