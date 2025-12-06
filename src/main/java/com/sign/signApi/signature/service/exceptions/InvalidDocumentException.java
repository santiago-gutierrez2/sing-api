package com.sign.signApi.signature.service.exceptions;

import com.sign.signApi.common.exceptions.ApiException;

public class InvalidDocumentException extends ApiException {
    public InvalidDocumentException(String message) {
        super(message);
    }
}
