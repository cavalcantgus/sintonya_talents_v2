package com.example.demo.dto;

import com.example.demo.enums.AuditAction;
import com.example.demo.enums.AuditStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogDTO {

    private String username;
    private Long userId;

    private AuditAction action;
    private String entity;
    private Long entityId;

    private String httpMethod;
    private String endpoint;
    private String ipAddress;

    private Object before;   // Object pois pode ser qualquer entidade
    private Object after;

    private AuditStatus status;
    private String errorMessage;
}
