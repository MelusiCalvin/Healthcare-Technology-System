package za.co.ubuntuhealth.identity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(schema = "audit", name = "audit_event")
public class AuditEvent {

    @Id
    private UUID id;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditOutcome outcome;

    @Column(name = "actor_user_id")
    private UUID actorUserId;

    @Column(name = "target_type")
    private String targetType;

    @Column(name = "target_id")
    private UUID targetId;

    @Column(name = "correlation_id")
    private String correlationId;

    @Column(name = "source_ip_hash")
    private String sourceIpHash;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> metadata;

    protected AuditEvent() {
    }

    private AuditEvent(
            String eventType,
            AuditOutcome outcome,
            UUID actorUserId,
            String targetType,
            UUID targetId,
            String correlationId,
            String sourceIpHash,
            Map<String, Object> metadata
    ) {
        this.id = UUID.randomUUID();
        this.occurredAt = Instant.now();
        this.eventType = eventType;
        this.outcome = outcome;
        this.actorUserId = actorUserId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.correlationId = correlationId;
        this.sourceIpHash = sourceIpHash;
        this.metadata = Map.copyOf(metadata);
    }

    public static AuditEvent authentication(
            String eventType,
            AuditOutcome outcome,
            UUID actorUserId,
            String correlationId,
            String sourceIpHash,
            Map<String, Object> metadata
    ) {
        return new AuditEvent(eventType, outcome, actorUserId, "USER_ACCOUNT", actorUserId, correlationId, sourceIpHash, metadata);
    }
}
