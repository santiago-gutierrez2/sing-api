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
    @Size(min = 4, message = "Password must be longer than 3 characters")
    private String password;
}
