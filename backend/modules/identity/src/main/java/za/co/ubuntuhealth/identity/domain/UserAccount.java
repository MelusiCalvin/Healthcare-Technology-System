package za.co.ubuntuhealth.identity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(schema = "iam", name = "user_account")
public class UserAccount {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Version
    private long version;

    protected UserAccount() {
    }

    private UserAccount(String username, String email, String displayName, UserStatus status) {
        this.id = UUID.randomUUID();
        this.username = Objects.requireNonNull(username, "username must not be null");
        this.email = email;
        this.displayName = Objects.requireNonNull(displayName, "displayName must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    public static UserAccount create(String username, String email, String displayName) {
        return new UserAccount(username, email, displayName, UserStatus.ACTIVE);
    }

    public UUID id() {
        return id;
    }

    public String username() {
        return username;
    }

    public String email() {
        return email;
    }

    public String displayName() {
        return displayName;
    }

    public UserStatus status() {
        return status;
    }

    public boolean mayAuthenticate() {
        return status == UserStatus.ACTIVE;
    }

    public void recordSuccessfulLogin(Instant occurredAt) {
        this.lastLoginAt = Objects.requireNonNull(occurredAt, "occurredAt must not be null");
    }
}
