package com.sign.signApi.user.service;

import com.sign.signApi.security.JwtUtil;
import com.sign.signApi.security.KeyEncryptionUtil;
import com.sign.signApi.user.dao.UserDAO;
import com.sign.signApi.user.dto.AuthResponseDTO;
import com.sign.signApi.user.dto.SignUpAndLoginDTO;
import com.sign.signApi.user.model.User;
import com.sign.signApi.user.service.exceptions.InvalidCredentialsException;
import com.sign.signApi.user.service.exceptions.UsernameAlreadyInUseException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final KeyEncryptionUtil keyEncryptionUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO signUp(SignUpAndLoginDTO signUpDTO) throws UsernameAlreadyInUseException {
        if (userDAO.existsByUsername(signUpDTO.getUsername())) {
            throw new UsernameAlreadyInUseException("User with username '" + signUpDTO.getUsername() + "' already exists");
        }

        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();

            String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

            String encryptedPrivateKey = keyEncryptionUtil.encrypt(privateKey);

            User user = User.builder()
                    .username(signUpDTO.getUsername())
                    .password(passwordEncoder.encode(signUpDTO.getPassword()))
                    .publicKey(publicKey)
                    .privateKey(encryptedPrivateKey)
                    .build();

            user = userDAO.save(user);

            // build response dto
            AuthResponseDTO responseDTO = new AuthResponseDTO();
            responseDTO.setUsername(user.getUsername());
            responseDTO.setId(user.getId());
            responseDTO.setToken(jwtUtil.generateToken(user.getUsername()));

            return responseDTO;
        } catch (Exception e) {
            String errorMessage = "An error occurred creating the user";
            log.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO login(SignUpAndLoginDTO signUpDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signUpDTO.getUsername(), signUpDTO.getPassword())
            );

            User user = userDAO.findByUsername(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

            AuthResponseDTO authResponseDTO = new AuthResponseDTO();
            authResponseDTO.setUsername(authentication.getName());
            authResponseDTO.setId(user.getId());
            authResponseDTO.setToken(jwtUtil.generateToken(authentication.getName()));

            return authResponseDTO;

        } catch (AuthenticationException e) {
            log.warn("Authentication failed for user {}", signUpDTO.getUsername());
            throw new InvalidCredentialsException();
        }
    }

}
