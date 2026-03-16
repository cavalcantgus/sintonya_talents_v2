package com.example.demo.repositories;

import com.example.demo.entities.ApplicationStageResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ApplicationStageResultRepository extends JpaRepository<ApplicationStageResult, Long> {

    @Query("""
    SELECT r FROM ApplicationStageResult r
    JOIN r.jobApplication ja
    JOIN ja.candidate c
    JOIN r.selectionStage s
    WHERE c.cpf = :cpf
    AND s.url = :url
    """)
    Optional<ApplicationStageResult> findStageResult(String cpf, String url);

    @Modifying
    @Query("""
    UPDATE ApplicationStageResult r
    SET r.locked = true
    WHERE r.jobApplication.id = :jobApplicationId
    AND r.id <> :stageResultId
    """)
    void lockOtherStages(Long jobApplicationId, Long stageResultId);

    @Modifying
    @Query("""
    UPDATE ApplicationStageResult r
    SET r.stageStatus = 'IN_PROGRESS'
    WHERE r.id = :stageResultId
    """)
    void startStage(Long stageResultId);
}
