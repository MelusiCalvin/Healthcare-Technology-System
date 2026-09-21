package za.co.ubuntuhealth.identity.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import za.co.ubuntuhealth.identity.domain.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {

    @Query("""
            select user from UserAccount user
            where lower(user.username) = lower(:identifier)
               or lower(user.email) = lower(:identifier)
            """)
    Optional<UserAccount> findByUsernameOrEmail(String identifier);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);
}
