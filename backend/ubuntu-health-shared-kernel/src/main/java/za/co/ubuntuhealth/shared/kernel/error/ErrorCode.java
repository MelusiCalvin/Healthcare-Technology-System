package za.co.ubuntuhealth.shared.kernel.error;

/**
 * Stable, machine-readable error codes shared across bounded contexts.
 */
public enum ErrorCode {
    VALIDATION_FAILED,
    BUSINESS_RULE_VIOLATION,
    RESOURCE_NOT_FOUND,
    CONFLICT,
    ACCESS_DENIED,
    INTERNAL_ERROR
}
