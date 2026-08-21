package za.co.ubuntuhealth.identity.infrastructure.persistence;

import za.co.ubuntuhealth.identity.domain.UserRoleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public interface UserRoleAssignmentRepository extends JpaRepository<UserRoleAssignment, UUID> {

    @Query("""
            select distinct assignment.role.code from UserRoleAssignment assignment
            where assignment.user.id = :userId
              and (assignment.expiresAt is null or assignment.expiresAt > :now)
            """)
    Set<String> findActiveRoleCodesByUserId(UUID userId, Instant now);
}
