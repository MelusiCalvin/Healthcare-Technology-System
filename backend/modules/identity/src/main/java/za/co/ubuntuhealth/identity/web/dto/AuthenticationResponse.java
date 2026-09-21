package za.co.ubuntuhealth.identity.web.dto;

import java.util.Set;
import java.util.UUID;

import za.co.ubuntuhealth.identity.domain.UserAccount;
import za.co.ubuntuhealth.identity.domain.UserRole;

public class AuthenticationResponse {

    private String accessToken;
    private String refreshToken;
    private UUID userId;
    private String username;
    private Set<UserRole> roles;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public AuthenticationResponse(String accessToken, String refreshToken, UserAccount user) {
        this(accessToken, refreshToken);
        this.userId = user.getId();
        this.username = user.getUsername();
        this.roles = user.getRoles();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }
}
