package za.co.ubuntuhealth.identity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(schema = "iam", name = "password_credential")
public class PasswordCredential {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    protected PasswordCredential() {
    }

    private PasswordCredential(UserAccount user, String passwordHash) {
        this.user = Objects.requireNonNull(user, "user must not be null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash must not be null");
    }

    public static PasswordCredential forUser(UserAccount user, String passwordHash) {
        return new PasswordCredential(user, passwordHash);
    }

    public UUID userId() {
        return userId;
    }

    public String passwordHash() {
        return passwordHash;
    }
}
