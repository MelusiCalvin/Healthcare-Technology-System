package za.co.ubuntuhealth.identity.application;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class TokenFactory {

    public String createAccessToken(UserAccountPrincipal principal) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString((principal.id() + "." + principal.username()).getBytes(StandardCharsets.UTF_8));
    }

    public String createRefreshToken() {
        return UUID.randomUUID().toString() + "." + UUID.randomUUID();
    }

    public String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashed);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is required for refresh token hashing.", exception);
        }
    }
}
