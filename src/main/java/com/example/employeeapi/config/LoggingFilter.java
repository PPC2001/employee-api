package com.example.employeeapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component

public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private static final String REQUEST_ID_HEADER = "X-Request-ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();
        // Add request ID to response headers
        response.setHeader(REQUEST_ID_HEADER, requestId);
        try {
            // Log incoming request
            logger.info("Incoming request: {} {} from {} (ID: {})",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getRemoteAddr(),
                    requestId);

            filterChain.doFilter(request, response);
        } finally {
            // Log response time
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Request completed: {} {} - Status: {} - Duration: {}ms (ID: {})",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration,
                    requestId);
        }
    }
}
