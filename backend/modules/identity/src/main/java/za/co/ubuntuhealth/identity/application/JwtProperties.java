package za.co.ubuntuhealth.identity.application;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ubuntu-health.security.jwt")
public class JwtProperties {

    private String issuer;
    private String audience;
    private String keyId;
    private String privateKeyBase64;
    private String publicKeyBase64;
    private Duration accessTokenTtl = Duration.ofMinutes(15);

    public String issuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String audience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String keyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String privateKeyBase64() {
        return privateKeyBase64;
    }

    public void setPrivateKeyBase64(String privateKeyBase64) {
        this.privateKeyBase64 = privateKeyBase64;
    }

    public String publicKeyBase64() {
        return publicKeyBase64;
    }

    public void setPublicKeyBase64(String publicKeyBase64) {
        this.publicKeyBase64 = publicKeyBase64;
    }

    public Duration accessTokenTtl() {
        return accessTokenTtl;
    }

    public void setAccessTokenTtl(Duration accessTokenTtl) {
        this.accessTokenTtl = accessTokenTtl;
    }
}
