package za.co.ubuntuhealth.identity.infrastructure.persistence;

import za.co.ubuntuhealth.identity.domain.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditEventRepository extends JpaRepository<AuditEvent, UUID> {
}
