package com.sign.signApi.signature.service.exceptions;

import com.sign.signApi.common.exceptions.ApiException;

public class KeyDecryptionException extends ApiException {
    public KeyDecryptionException(String message) {
        super(message);
    }
}
