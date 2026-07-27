package za.co.ubuntuhealth.shared.kernel.error;

import java.util.Objects;

/**
 * Base exception for expected domain failures. Its message must be safe to expose to an authorised API caller.
 */
public class DomainException extends RuntimeException {

    private final ErrorCode errorCode;

    public DomainException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = Objects.requireNonNull(errorCode, "errorCode must not be null");
    }

    public ErrorCode errorCode() {
        return errorCode;
    }
}
