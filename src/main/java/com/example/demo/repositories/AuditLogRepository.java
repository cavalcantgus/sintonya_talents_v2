package com.example.demo.repositories;

import com.example.demo.dto.AuditLogDTO;
import com.example.demo.entities.AuditLog;
import com.example.demo.enums.AuditAction;
import com.example.demo.enums.AuditStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Busca por usuário
    List<AuditLog> findByUsernameOrderByDateTimeDesc(String username);

//    // Busca por entidade + id (ex: todo histórico do Product de id 42)
//    List<AuditLog> findByEntityAndEntityIdOrderByDateTimeDesc(String entity, Long entityId);

    // Busca por ação (ex: todos os DELETEs)
    List<AuditLog> findByActionOrderByDateTimeDesc(AuditAction action);

    // Busca por período
    List<AuditLog> findByDateTimeBetweenOrderByDateTimeDesc(
            LocalDateTime start, LocalDateTime end
    );

    // Busca por status (ex: todas as FAILUREs)
    List<AuditLog> findByStatusOrderByDateTimeDesc(AuditStatus status);

    // --- Consultas mais elaboradas com @Query ---

    // Histórico completo de uma entidade em um período
//    @Query("""
//        SELECT a FROM AuditLog a
//        WHERE a.entity = :entity
//          AND (:entityId IS NULL OR a.entityId = :entityId)
//          AND a.dateTime BETWEEN :start AND :end
//        ORDER BY a.dateTime DESC
//    """)
//    List<AuditLog> findByEntityAndPeriod(
//            @Param("entity") String entity,
//            @Param("entityId") Long entityId,
//            @Param("start") LocalDateTime start,
//            @Param("end") LocalDateTime end
//    );

    // Filtro geral combinado (para tela de consulta com vários filtros opcionais)
    @Query("""
        SELECT a FROM AuditLog a
        WHERE (:username IS NULL OR a.username = :username)
          AND (:action   IS NULL OR a.action   = :action)
          AND (:entity   IS NULL OR a.entity   = :entity)
          AND (:status   IS NULL OR a.status   = :status)
          AND (:start    IS NULL OR a.dateTime >= :start)
          AND (:end      IS NULL OR a.dateTime <= :end)
        ORDER BY a.dateTime DESC
    """)
    Page<AuditLog> search(
            @Param("username") String username,
            @Param("action")   AuditAction action,
            @Param("entity")   String entity,
            @Param("status")   AuditStatus status,
            @Param("start")    LocalDateTime start,
            @Param("end")      LocalDateTime end,
            Pageable pageable
    );
}
