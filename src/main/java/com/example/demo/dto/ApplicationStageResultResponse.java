package com.example.demo.dto;

import com.example.demo.entities.ApplicationStageResult;
import com.example.demo.entities.Vacancy;

public record ApplicationStageResultResponse(
        Long id,
        Long selectionStageId,
        String stageName,
        String stageStatus,
        String url,
        Integer stageOrder,
        boolean locked
) {
    public static ApplicationStageResultResponse fromEntity(ApplicationStageResult app) {
        return new ApplicationStageResultResponse(
             app.getId(),
             app.getSelectionStage().getId(),
             app.getSelectionStage().getName(),
             app.getStageStatus().toString(),
                app.getSelectionStage().getUrl(),
                app.getSelectionStage().getStageOrder(),
                app.isLocked()
        );
    }
}
