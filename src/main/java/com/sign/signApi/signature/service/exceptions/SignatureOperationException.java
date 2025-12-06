package com.sign.signApi.signature.service.exceptions;

import com.sign.signApi.common.exceptions.ApiException;

public class SignatureOperationException extends ApiException {
    public SignatureOperationException(String message) {
        super(message);
    }
}
