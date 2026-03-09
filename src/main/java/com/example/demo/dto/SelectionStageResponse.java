package com.example.demo.dto;

import com.example.demo.entities.SelectionStage;

public record SelectionStageResponse(
        String name,
        String stageType,
        String url,
        Integer stageOrder
) {
    public static SelectionStageResponse fromEntity(SelectionStage selectionStage) {
        return new SelectionStageResponse(
                selectionStage.getName(),
                selectionStage.getStageType() != null ? selectionStage.getStageType().toString() : null,
                selectionStage.getUrl(),
                selectionStage.getStageOrder()
        );
    }
}
