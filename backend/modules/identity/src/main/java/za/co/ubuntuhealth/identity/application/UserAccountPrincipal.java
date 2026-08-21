package za.co.ubuntuhealth.identity.application;

import java.util.UUID;

import za.co.ubuntuhealth.identity.domain.UserAccount;

public class UserAccountPrincipal {

    private final UUID id;
    private final String username;

    public UserAccountPrincipal(UserAccount user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

    public UUID id() {
        return id;
    }

    public String username() {
        return username;
    }
}
