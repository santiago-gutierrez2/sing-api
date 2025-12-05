package com.sign.signApi.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDTO {
    private int status;
    private String error;
    private String timestamp;
    private String path;
}
