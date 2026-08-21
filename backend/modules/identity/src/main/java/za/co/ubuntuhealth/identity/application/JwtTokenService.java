package za.co.ubuntuhealth.identity.application;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

import org.springframework.stereotype.Component;

import za.co.ubuntuhealth.identity.domain.UserAccount;

@Component
public class JwtTokenService {

    private final JwtProperties properties;

    public JwtTokenService(JwtProperties properties) {
        this.properties = properties;
    }

    public String createAccessToken(UserAccount user) {
        Map<String, Object> payload = Map.of(
                "iss", properties.issuer(),
                "aud", properties.audience(),
                "sub", user.getId().toString(),
                "preferred_username", user.getUsername(),
                "iat", Instant.now().getEpochSecond(),
                "exp", Instant.now().plus(properties.accessTokenTtl()).getEpochSecond()
        );

        String header = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
        String body = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.toString().getBytes());
        String signature = Base64.getUrlEncoder().withoutPadding().encodeToString((header + "." + body).getBytes());
        return String.join(".", header, body, signature);
    }
}
