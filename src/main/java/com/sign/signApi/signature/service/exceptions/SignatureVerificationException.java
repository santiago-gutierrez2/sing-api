package com.sign.signApi.signature.service.exceptions;

import com.sign.signApi.common.exceptions.ApiException;

public class SignatureVerificationException extends ApiException {
    public SignatureVerificationException(String message) {
        super(message);
    }
}
