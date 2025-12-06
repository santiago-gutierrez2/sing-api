package com.sign.signApi.signature.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DocumentToSignDTO {
    @NotBlank(message = "Document cannot be empty")
    private String documentBase64;
}
