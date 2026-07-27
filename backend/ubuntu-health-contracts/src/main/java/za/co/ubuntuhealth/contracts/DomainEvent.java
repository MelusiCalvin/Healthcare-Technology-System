package za.co.ubuntuhealth.contracts;

import java.time.Instant;
import java.util.UUID;

/**
 * Public event contract for asynchronous communication between bounded contexts.
 */
public interface DomainEvent {

    UUID eventId();

    Instant occurredAt();

    String eventType();
}
