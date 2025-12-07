package com.sign.signApi.signature.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifySignatureDTO {
    @NotNull(message = "the user id cannot be null")
    private Long userId;
    @NotBlank(message = "Document cannot be empty")
    @Size(max = 35000000, message = "Document too large")
    private String documentBase64;
    @NotBlank(message = "Signature cannot be empty")
    private String signatureBase64;
}
