package za.co.ubuntuhealth.bootstrap.web.error;

import za.co.ubuntuhealth.bootstrap.web.filter.CorrelationIdFilter;
import za.co.ubuntuhealth.shared.kernel.error.DomainException;
import za.co.ubuntuhealth.shared.kernel.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Converts expected failures to RFC 9457 Problem Details and prevents internal details leaking to callers.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String PROBLEM_NAMESPACE = "urn:ubuntu-health:problem:";

    @ExceptionHandler(DomainException.class)
    ResponseEntity<ProblemDetail> handleDomainException(DomainException exception, HttpServletRequest request) {
        HttpStatus status = switch (exception.errorCode()) {
            case RESOURCE_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
            case ACCESS_DENIED -> HttpStatus.FORBIDDEN;
            case VALIDATION_FAILED, BUSINESS_RULE_VIOLATION -> HttpStatus.BAD_REQUEST;
            case INTERNAL_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return ResponseEntity.status(status)
                .body(problem(status, exception.errorCode(), exception.getMessage(), request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        Map<String, String> violations = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                violations.putIfAbsent(error.getField(), error.getDefaultMessage()));

        ProblemDetail problem = problem(
                HttpStatus.BAD_REQUEST,
                ErrorCode.VALIDATION_FAILED,
                "One or more fields are invalid.",
                request
        );
        problem.setProperty("violations", violations);
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler({ConstraintViolationException.class, HttpMessageNotReadableException.class, IllegalArgumentException.class})
    ResponseEntity<ProblemDetail> handleMalformedRequest(Exception exception, HttpServletRequest request) {
        return ResponseEntity.badRequest().body(problem(
                HttpStatus.BAD_REQUEST,
                ErrorCode.VALIDATION_FAILED,
                "The request is invalid.",
                request
        ));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ProblemDetail> handleUnexpectedException(Exception exception, HttpServletRequest request) {
        String correlationId = CorrelationIdFilter.currentCorrelationId();
        LOGGER.error("Unhandled request failure. correlationId={}", correlationId, exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCode.INTERNAL_ERROR,
                "An unexpected error occurred. Please provide the correlation ID to support if the problem persists.",
                request
        ));
    }

    private ProblemDetail problem(
            HttpStatus status,
            ErrorCode errorCode,
            String detail,
            HttpServletRequest request
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(titleFor(errorCode));
        problem.setType(URI.create(PROBLEM_NAMESPACE + errorCode.name().toLowerCase()));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("code", errorCode.name());
        problem.setProperty("correlationId", CorrelationIdFilter.currentCorrelationId());
        return problem;
    }

    private String titleFor(ErrorCode errorCode) {
        return switch (errorCode) {
            case VALIDATION_FAILED -> "Validation failed";
            case BUSINESS_RULE_VIOLATION -> "Business rule violation";
            case RESOURCE_NOT_FOUND -> "Resource not found";
            case CONFLICT -> "Conflict";
            case ACCESS_DENIED -> "Access denied";
            case INTERNAL_ERROR -> "Internal server error";
        };
    }
}
