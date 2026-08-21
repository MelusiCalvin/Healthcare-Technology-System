package za.co.ubuntuhealth.identity.infrastructure.persistence;

import za.co.ubuntuhealth.identity.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Modifying
    @Query("""
            update RefreshToken token
            set token.revokedAt = :revokedAt, token.revocationReason = :reason
            where token.familyId = :familyId and token.revokedAt is null
            """)
    int revokeActiveFamily(@Param("familyId") UUID familyId, @Param("reason") String reason, @Param("revokedAt") Instant revokedAt);
}
