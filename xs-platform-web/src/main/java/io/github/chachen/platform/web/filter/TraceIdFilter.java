package io.github.chachen.platform.web.filter;

import io.github.chachen.platform.core.context.TraceContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TraceIdFilter extends OncePerRequestFilter {
    public static final String HEADER = "X-Request-Id";
    public static final String LEGACY_HEADER = "X-Trace-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String traceId = request.getHeader(HEADER);
        if (traceId == null || traceId.isBlank()) traceId = request.getHeader(LEGACY_HEADER);
        if (traceId == null || traceId.isBlank() || traceId.length() > 64) traceId = TraceContext.getOrCreate();
        TraceContext.set(traceId);
        MDC.put(HEADER, traceId);
        MDC.put(LEGACY_HEADER, traceId);
        response.setHeader(HEADER, traceId);
        response.setHeader(LEGACY_HEADER, traceId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(HEADER);
            MDC.remove(LEGACY_HEADER);
            TraceContext.clear();
        }
    }
}
