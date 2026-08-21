package za.co.ubuntuhealth.bootstrap.web;

import java.time.Instant;

/**
 * Standard envelope for successful API responses. Errors use RFC 9457 Problem Details.
 */
public class ApiResponse<T> {
    private final T data;
    private final Instant timestamp;
    private final String correlationId;

    public ApiResponse(T data, Instant timestamp, String correlationId) {
        this.data = data;
        this.timestamp = timestamp;
        this.correlationId = correlationId;
    }

    public T getData() {
        return data;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public static <T> ApiResponse<T> success(T data, String correlationId) {
        return new ApiResponse<>(data, Instant.now(), correlationId);
    }
}
