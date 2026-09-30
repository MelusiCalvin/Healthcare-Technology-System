package za.co.ubuntuhealth.identity.web.dto;

import java.util.Set;
import java.util.UUID;

import za.co.ubuntuhealth.identity.domain.UserRole;

public class AuthenticationResponse {

    private String accessToken;
    private String refreshToken;
    private UUID userId;
    private String username;
    private Set<UserRole> roles;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(
            String accessToken,
            String refreshToken,
            UUID userId,
            String username,
            Set<UserRole> roles
    ) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.username = username;
        this.roles = roles;
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

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }
}
