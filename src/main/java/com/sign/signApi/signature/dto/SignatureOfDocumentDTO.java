package com.sign.signApi.signature.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignatureOfDocumentDTO {
    private String signatureBase64;
}
