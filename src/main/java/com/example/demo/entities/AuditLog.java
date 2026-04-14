package com.example.demo.entities;

import com.example.demo.enums.AuditAction;
import com.example.demo.enums.AuditStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log", indexes = {
        @Index(name = "idx_audit_user", columnList = "userId"),
        @Index(name = "idx_audit_datetime", columnList = "dateTime")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Guarda só o username para não quebrar se o user for deletado
    @Column(name = "username")
    private String username;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private AuditAction action;        // CREATE, UPDATE, DELETE, READ, LOGIN, etc.

    private String entity;             // "Product", "Order", "User"

    private String httpMethod;         // GET, POST, PUT, DELETE
    private String endpoint;           // /api/products/42
    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String before;             // JSON do estado anterior

    @Column(columnDefinition = "TEXT")
    private String after;              // JSON do estado posterior

    @Column(columnDefinition = "TEXT")
    private String errorMessage;       // se a operação falhou

    @Enumerated(EnumType.STRING)
    private AuditStatus status;        // SUCCESS, FAILURE

    private LocalDateTime dateTime;

    @PrePersist
    public void prePersist() {
        this.dateTime = LocalDateTime.now();
    }
}