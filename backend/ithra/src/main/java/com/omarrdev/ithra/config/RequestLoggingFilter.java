package com.omarrdev.ithra.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if (!path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper wrapped = new ContentCachingRequestWrapper(request, 8192);
        filterChain.doFilter(wrapped, response);

        byte[] buf = wrapped.getContentAsByteArray();
        if (buf.length > 0) {
            String body = new String(buf, StandardCharsets.UTF_8);
            log.info(">>> {} {}  Content-Type={}  Body={}",
                    request.getMethod(), path, request.getContentType(), body);
        } else {
            log.info(">>> {} {}  (empty body)  Content-Type={}",
                    request.getMethod(), path, request.getContentType());
        }
    }
}
