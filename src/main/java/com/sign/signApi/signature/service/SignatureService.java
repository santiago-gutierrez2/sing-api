package com.sign.signApi.signature.service;

import com.sign.signApi.common.exceptions.ApiException;
import com.sign.signApi.security.KeyEncryptionUtil;
import com.sign.signApi.signature.dto.DocumentToSignDTO;
import com.sign.signApi.signature.dto.SignatureOfDocumentDTO;
import com.sign.signApi.signature.dto.VerificationResultDTO;
import com.sign.signApi.signature.dto.VerifySignatureDTO;
import com.sign.signApi.signature.service.exceptions.*;
import com.sign.signApi.user.dao.UserDAO;
import com.sign.signApi.user.model.User;
import com.sign.signApi.user.service.exceptions.UnauthorizedException;
import com.sign.signApi.user.service.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SignatureService {
    private final UserDAO userDAO;
    private final KeyEncryptionUtil keyEncryptionUtil;

    public SignatureOfDocumentDTO signDocument(DocumentToSignDTO documentToSignDTO) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
                throw new UnauthorizedException("User not logged in");
            }
            String username = auth.getName();

            User user = userDAO.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found: " + username));

            if (user.getPrivateKey() == null || user.getPrivateKey().isBlank()) {
                throw new KeyNotFoundException("User '" + username + "' does not have a private key");
            }

            String decryptedPrivateKeyBase64;
            try {
                decryptedPrivateKeyBase64 = keyEncryptionUtil.decrypt(user.getPrivateKey());
            } catch (Exception e) {
                log.error("Could not decrypt private key", e);
                throw new KeyDecryptionException("Could not decrypt private key");
            }

            PrivateKey privateKey;
            try {
                byte[] decodedPrivateKey = Base64.getDecoder().decode(decryptedPrivateKeyBase64);
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedPrivateKey);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                privateKey = keyFactory.generatePrivate(keySpec);
            } catch (GeneralSecurityException | IllegalArgumentException e) {
                log.error("Invalid private key format");
                throw new InvalidKeyFormatException("Invalid private key format");
            }

            byte[] documentBytes;
            try {
                documentBytes = Base64.getDecoder().decode(documentToSignDTO.getDocumentBase64());
            } catch (IllegalArgumentException e) {
                log.error("Invalid document Base64");
                throw new InvalidDocumentException("Invalid document Base64");
            }

            byte[] signatureBytes;
            try {
                Signature signature = Signature.getInstance("SHA256withRSA");
                signature.initSign(privateKey);
                signature.update(documentBytes);
                signatureBytes = signature.sign();
            } catch (GeneralSecurityException e) {
                log.error("Failed to generate signature", e);
                throw new SignatureOperationException("Failed to generate signature");
            }

            return new SignatureOfDocumentDTO(Base64.getEncoder().encodeToString(signatureBytes));

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during document signing", e);
            throw new SignatureOperationException("Unexpected error during signing");
        }
    }

    public VerificationResultDTO verifySignature(VerifySignatureDTO verifySignatureDTO) {
        try {
            User user = userDAO.findById(verifySignatureDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            PublicKey publicKey;
            try {
                byte[] publicKeyBytes = Base64.getDecoder().decode(user.getPublicKey());
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                publicKey = keyFactory.generatePublic(keySpec);
            } catch (GeneralSecurityException | IllegalArgumentException e) {
                log.error("Invalid public key format");
                throw new InvalidKeyFormatException("Invalid public key format");
            }

            byte[] documentBytes;
            byte[] signatureBytes;
            try {
                documentBytes = Base64.getDecoder().decode(verifySignatureDTO.getDocumentBase64());
                signatureBytes = Base64.getDecoder().decode(verifySignatureDTO.getSignatureBase64());
            } catch (IllegalArgumentException e) {
                log.error("Invalid document/signature Base64");
                throw new InvalidDocumentException("Invalid document/signature Base64");
            }

            boolean isValid = false;
            try {
                Signature signature = Signature.getInstance("SHA256withRSA");
                signature.initVerify(publicKey);
                signature.update(documentBytes);
                isValid = signature.verify(signatureBytes);
            } catch (Exception e) {
                log.error("Signature verification failed", e);
                throw new SignatureVerificationException("Signature verification failed");
            }

            return new VerificationResultDTO(isValid);

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during signature verification", e);
            throw new SignatureOperationException("Unexpected error during signature verification");
        }
    }
}
