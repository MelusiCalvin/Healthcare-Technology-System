package za.co.ubuntuhealth.bootstrap.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Establishes one safe correlation identifier per request and places it in logs and responses.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class CorrelationIdFilter extends OncePerRequestFilter {

    public static final String HEADER_NAME = "X-Correlation-ID";
    private static final String MDC_KEY = "correlationId";
    private static final Pattern VALID_CORRELATION_ID = Pattern.compile("[A-Za-z0-9-]{8,128}");

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String correlationId = correlationIdFrom(request);
        MDC.put(MDC_KEY, correlationId);
        response.setHeader(HEADER_NAME, correlationId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_KEY);
        }
    }

    public static String currentCorrelationId() {
        String value = MDC.get(MDC_KEY);
        return value == null ? "unavailable" : value;
    }

    private String correlationIdFrom(HttpServletRequest request) {
        String requestedId = request.getHeader(HEADER_NAME);
        return requestedId != null && VALID_CORRELATION_ID.matcher(requestedId).matches()
                ? requestedId
                : UUID.randomUUID().toString();
    }
}
