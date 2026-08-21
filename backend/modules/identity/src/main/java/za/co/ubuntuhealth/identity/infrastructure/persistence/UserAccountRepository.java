package za.co.ubuntuhealth.identity.infrastructure.persistence;

import za.co.ubuntuhealth.identity.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {

    @Query("""
            select user from UserAccount user
            where lower(user.username) = lower(:identifier)
               or lower(user.email) = lower(:identifier)
            """)
    Optional<UserAccount> findByUsernameOrEmail(String identifier);

    boolean existsByUsernameIgnoreCase(String username);
}
