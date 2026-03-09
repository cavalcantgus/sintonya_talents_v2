package com.example.demo.dto;

import com.example.demo.entities.ApplicationStageResult;
import com.example.demo.entities.Vacancy;

public record ApplicationStageResultResponse(
        Long id,
        String stageStatus,
        boolean locked
) {
    public static ApplicationStageResultResponse fromEntity(ApplicationStageResult app) {
        return new ApplicationStageResultResponse(
             app.getId(),
             app.getStageStatus().toString(),
                app.isLocked()
        );
    }
}
