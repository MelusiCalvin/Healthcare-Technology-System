package za.co.ubuntuhealth.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.ubuntuhealth.identity.domain.UserAccount;

import java.util.Optional;
import java.util.UUID;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {
    Optional<UserAccount> findByUsername(String username);
    boolean existsByUsername(String username);
}
