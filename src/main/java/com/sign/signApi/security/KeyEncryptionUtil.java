package com.sign.signApi.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
@Slf4j
public class KeyEncryptionUtil {

    @Value("${keys-encryption.secret-key}")
    private String secretKey;

    private SecretKeySpec getSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            log.error("Error while encrypting data", e);
            throw new RuntimeException("Error while encrypting data", e);
        }
    }

    public String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            return new String(cipher.doFinal(decodedBytes));
        } catch (Exception e) {
            log.error("Error while decrypting data", e);
            throw new RuntimeException("Error while decrypting data", e);
        }
    }
}
