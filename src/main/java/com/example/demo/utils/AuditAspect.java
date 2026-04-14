package com.example.demo.utils;

import com.example.demo.dto.AuditLogDTO;
import com.example.demo.entities.User;
import com.example.demo.enums.AuditStatus;
import com.example.demo.interfaces.Auditable;
import com.example.demo.services.AuditService;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditService auditService;
    private final AuditRequestContext requestContext;
    private final UserService userService;

    // Intercepta qualquer método anotado com @Auditable
    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {

        // --- Dados do usuário autenticado ---
        String username = "anonymous";
        Long userId = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        }

        // --- Before: captura o primeiro argumento como estado anterior ---
        Object[] args = joinPoint.getArgs();
        Object beforeState = (auditable.captureArgs() && args.length > 0) ? args[0] : null;
        Object afterState = null;
        AuditStatus status = AuditStatus.SUCCESS;
        String errorMessage = null;

        try {
            afterState = joinPoint.proceed(); // executa o método original
            return afterState;
        } catch (Throwable ex) {
            status = AuditStatus.FAILURE;
            errorMessage = ex.getMessage();
            throw ex; // repropaga a exceção normalmente
        } finally {
            // Roda SEMPRE, com sucesso ou falha
            auditService.log(AuditLogDTO.builder()
                    .username(username)
                    .userId(userId)
                    .action(auditable.action())
                    .entity(auditable.entity())
                    .httpMethod(safeGetHttpMethod())
                    .endpoint(safeGetEndpoint())
                    .ipAddress(safeGetIp())
                    .before(beforeState)
                    .after(status == AuditStatus.SUCCESS ? afterState : null)
                    .status(status)
                    .errorMessage(errorMessage)
                    .build());
        }
    }

    // Helpers para não estourar se chamado fora de um request HTTP
    private String safeGetHttpMethod() {
        try { return requestContext.getHttpMethod(); } catch (Exception e) { return null; }
    }
    private String safeGetEndpoint() {
        try { return requestContext.getEndpoint(); } catch (Exception e) { return null; }
    }
    private String safeGetIp() {
        try { return requestContext.getIpAddress(); } catch (Exception e) { return null; }
    }
}
