package com.sign.signApi.user.web;

import com.sign.signApi.user.dto.AuthResponseDTO;
import com.sign.signApi.user.dto.SignUpAndLoginDTO;
import com.sign.signApi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/signup")
    public AuthResponseDTO signUp(@Valid @RequestBody SignUpAndLoginDTO signUpDTO) {
        return userService.signUp(signUpDTO);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody SignUpAndLoginDTO loginDTO) {
        return userService.login(loginDTO);
    }
}
