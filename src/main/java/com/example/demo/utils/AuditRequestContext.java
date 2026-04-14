package com.example.demo.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditRequestContext {

    private final HttpServletRequest request;

    public String getIpAddress() {
        String ip = request.getHeader("CF-Connecting-IP");

        if (ip == null || ip.isBlank()) {
            ip = request.getHeader("X-Forwarded-For");
            if (ip != null && !ip.isBlank()) {
                return ip.split(",")[0].trim();
            }
        }

        if (ip == null || ip.isBlank()) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public String getEndpoint() {
        return request.getRequestURI();
    }

    public String getHttpMethod() {
        return request.getMethod();
    }
}
