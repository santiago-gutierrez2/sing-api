package com.sign.signApi.signature.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DocumentToSignDTO {
    @NotBlank(message = "Document cannot be empty")
    @Size(max = 35000000, message = "Document too large")
    private String documentBase64;
}
