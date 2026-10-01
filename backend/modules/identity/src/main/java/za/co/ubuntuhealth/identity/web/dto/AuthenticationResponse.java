package za.co.ubuntuhealth.identity.web.dto;

import java.util.Set;
import java.util.UUID;

import za.co.ubuntuhealth.identity.domain.UserRole;

public class AuthenticationResponse {

    private String accessToken;
    private String refreshToken;
    private UUID userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Set<UserRole> roles;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(
            String accessToken,
            String refreshToken,
            UUID userId,
            String username,
            String firstName,
            String lastName,
            String email,
            Set<UserRole> roles
    ) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }
}
