package za.co.ubuntuhealth.identity.infrastructure.persistence;

import za.co.ubuntuhealth.identity.domain.LoginAttempt;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select attempt from LoginAttempt attempt where attempt.subjectHash = :subjectHash")
    Optional<LoginAttempt> findBySubjectHashForUpdate(String subjectHash);
}
