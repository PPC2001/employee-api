package com.example.employeeapi.config;


import com.example.employeeapi.exception.InvalidApiKeyException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Slf4j
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final String apiKey;
    private static final String API_KEY_HEADER = "X-API-Key";
    private static final List<String> PROTECTED_PATHS = List.of("/api/employees", "/api/admin");

    public ApiKeyAuthFilter(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        boolean isProtected = PROTECTED_PATHS.stream().anyMatch(path::startsWith);
        if (isProtected) {
            String requestApiKey = request.getHeader(API_KEY_HEADER);
            if (requestApiKey == null || !apiKey.equals(requestApiKey)) {
                throw new InvalidApiKeyException("Invalid or missing API key");
            }
            Authentication auth = new ApiKeyAuthentication(requestApiKey, AuthorityUtils.NO_AUTHORITIES);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
