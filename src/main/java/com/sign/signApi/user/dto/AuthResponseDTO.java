package com.sign.signApi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private Long id;
    private String username;
    private String token;
}
