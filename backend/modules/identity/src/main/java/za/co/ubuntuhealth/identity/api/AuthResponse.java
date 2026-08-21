package za.co.ubuntuhealth.identity.api;

import za.co.ubuntuhealth.identity.domain.UserAccount;
import za.co.ubuntuhealth.identity.domain.UserRole;

import java.util.Set;
import java.util.UUID;

public record AuthResponse(
        UUID userId,
        String username,
        Set<UserRole> roles
) {
    public static AuthResponse from(UserAccount user) {
        return new AuthResponse(user.getId(), user.getUsername(), user.getRoles());
    }
}
