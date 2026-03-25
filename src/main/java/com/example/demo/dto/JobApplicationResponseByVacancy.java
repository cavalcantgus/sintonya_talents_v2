package com.example.demo.dto;

import com.example.demo.entities.Candidate;
import com.example.demo.entities.JobApplication;

import java.util.Comparator;
import java.util.List;

public record JobApplicationResponseByVacancy(
        Long id,
        VacancyResponse vacancyResponse,
        List<ApplicationStageResultResponse> stages,
        ApplicationStageResultResponse currentStage,
        CandidateResponse candidate
) {

    public static JobApplicationResponseByVacancy fromEntity(JobApplication jobApplication) {

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

        return new JobApplicationResponseByVacancy(
                jobApplication.getId(),
                VacancyResponse.fromEntity(jobApplication.getVacancy()),
                stages,
                currentStage,
                CandidateResponse.fromEntity(jobApplication.getCandidate())
        );
    }
}
