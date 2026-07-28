package za.co.ubuntuhealth.identity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(schema = "iam", name = "refresh_token")
public class RefreshToken {

    @Id
    private UUID id;

    @Column(name = "token_hash", nullable = false, unique = true)
    private String tokenHash;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @Column(name = "family_id", nullable = false)
    private UUID familyId;

    @Column(name = "replaced_by_token_id")
    private UUID replacedByTokenId;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "revocation_reason")
    private String revocationReason;

    @Column(name = "user_agent_hash")
    private String userAgentHash;

    @Column(name = "source_ip_hash")
    private String sourceIpHash;

    protected RefreshToken() {
    }

    private RefreshToken(
            UserAccount user,
            String tokenHash,
            UUID familyId,
            Instant expiresAt,
            String userAgentHash,
            String sourceIpHash
    ) {
        this.id = UUID.randomUUID();
        this.user = Objects.requireNonNull(user, "user must not be null");
        this.tokenHash = Objects.requireNonNull(tokenHash, "tokenHash must not be null");
        this.familyId = Objects.requireNonNull(familyId, "familyId must not be null");
        this.expiresAt = Objects.requireNonNull(expiresAt, "expiresAt must not be null");
        this.userAgentHash = userAgentHash;
        this.sourceIpHash = sourceIpHash;
    }

    public static RefreshToken issue(
            UserAccount user,
            String tokenHash,
            UUID familyId,
            Instant expiresAt,
            String userAgentHash,
            String sourceIpHash
    ) {
        return new RefreshToken(user, tokenHash, familyId, expiresAt, userAgentHash, sourceIpHash);
    }

    public UUID id() {
        return id;
    }

    public UserAccount user() {
        return user;
    }

    public UUID familyId() {
        return familyId;
    }

    public boolean isExpired(Instant now) {
        return !expiresAt.isAfter(now);
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public void revoke(RefreshTokenRevocationReason reason, Instant occurredAt) {
        if (revokedAt == null) {
            revokedAt = Objects.requireNonNull(occurredAt, "occurredAt must not be null");
            revocationReason = Objects.requireNonNull(reason, "reason must not be null").name();
        }
    }

    public void replaceWith(UUID replacementTokenId, Instant occurredAt) {
        revoke(RefreshTokenRevocationReason.ROTATED, occurredAt);
        replacedByTokenId = Objects.requireNonNull(replacementTokenId, "replacementTokenId must not be null");
    }
}
