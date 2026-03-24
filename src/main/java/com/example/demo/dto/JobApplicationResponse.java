package com.example.demo.dto;

import com.example.demo.entities.JobApplication;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record JobApplicationResponse(
        Long id,
        VacancyResponse vacancyResponse,
        List<ApplicationStageResultResponse> stages,
        ApplicationStageResultResponse currentStage
) {

    public static JobApplicationResponse fromEntity(JobApplication jobApplication) {

        List<ApplicationStageResultResponse> stages =
                jobApplication.getApplicationStageResults()
                        .stream()
                        .filter(a -> a.getSelectionStage() != null
                                && a.getSelectionStage().getStageOrder() != null)
                        .sorted(Comparator.comparing(a -> a.getSelectionStage().getStageOrder()))
                        .map(ApplicationStageResultResponse::fromEntity)
                        .toList();

        ApplicationStageResultResponse currentStage =
                stages.stream()
                        .filter(stage -> !stage.locked())
                        .findFirst()
                        .orElse(null);

        return new JobApplicationResponse(
                jobApplication.getId(),
                VacancyResponse.fromEntity(jobApplication.getVacancy()),
                stages,
                currentStage
        );
    }
}
