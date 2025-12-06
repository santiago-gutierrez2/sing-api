package com.sign.signApi.signature.service.exceptions;

import com.sign.signApi.common.exceptions.ApiException;

public class InvalidKeyFormatException extends ApiException {
    public InvalidKeyFormatException(String message) {
        super(message);
    }
}
