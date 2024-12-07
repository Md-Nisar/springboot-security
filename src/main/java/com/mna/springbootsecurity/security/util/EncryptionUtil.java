package com.mna.springbootsecurity.security.util;

import com.mna.springbootsecurity.security.exception.EncryptionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding"; // Use GCM mode
    private static final int GCM_TAG_LENGTH = 16; // Length of the authentication tag
    private static final int IV_SIZE_BYTES = 12; // Recommended IV size for GCM

    @Value("${application.encryption.secret-key}")
    private String secretKey;

    public String encrypt(String plainText) {
        try {
            SecretKey key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            byte[] iv = generateIv();
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv));
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            // Combine IV and encrypted bytes
            byte[] combined = new byte[IV_SIZE_BYTES + encryptedBytes.length];
            System.arraycopy(iv, 0, combined, 0, IV_SIZE_BYTES);
            System.arraycopy(encryptedBytes, 0, combined, IV_SIZE_BYTES, encryptedBytes.length);
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new EncryptionException("Encryption failed for the input: [PROTECTED]", e);
        }
    }

    public String decrypt(String cipherText) {
        try {
            byte[] combined = Base64.getDecoder().decode(cipherText);
            byte[] iv = new byte[IV_SIZE_BYTES];
            System.arraycopy(combined, 0, iv, 0, IV_SIZE_BYTES);
            byte[] encryptedBytes = new byte[combined.length - IV_SIZE_BYTES];
            System.arraycopy(combined, IV_SIZE_BYTES, encryptedBytes, 0, encryptedBytes.length);

            SecretKey key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv));
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new EncryptionException("Decryption failed for the input: [PROTECTED]", e);
        }
    }

    private byte[] generateIv() {
        byte[] iv = new byte[IV_SIZE_BYTES];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
}
