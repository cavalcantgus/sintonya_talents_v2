package com.example.demo.services;

import com.example.demo.dto.AuditLogDTO;
import com.example.demo.entities.AuditLog;
import com.example.demo.repositories.AuditLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    public void log(AuditLogDTO dto) {
        try {
            AuditLog log = AuditLog.builder()
                    .username(dto.getUsername())
                    .userId(dto.getUserId())
                    .action(dto.getAction())
                    .entity(dto.getEntity())
                    .httpMethod(dto.getHttpMethod())
                    .endpoint(dto.getEndpoint())
                    .ipAddress(dto.getIpAddress())
                    .before(toJson(dto.getBefore()))
                    .after(toJson(dto.getAfter()))
                    .status(dto.getStatus())
                    .errorMessage(dto.getErrorMessage())
                    .build();

            auditLogRepository.save(log);
        } catch (Exception e) {
            // NUNCA deixe o log derrubar a operação principal
            log.error("Falha ao salvar audit log: {}", e.getMessage());
        }
    }

    private String toJson(Object obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return obj.toString();
        }
    }
}
