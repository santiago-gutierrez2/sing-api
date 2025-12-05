package com.sign.signApi.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpAndLoginDTO {
    @NotBlank(message = "Username cannot be empty")
    @Size(max = 50, message = "Username cannot be longer than 50 characters")
    private String username;
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
