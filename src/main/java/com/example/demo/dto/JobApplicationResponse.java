package com.example.demo.dto;

import com.example.demo.entities.JobApplication;

import java.util.Set;
import java.util.stream.Collectors;

public record JobApplicationResponse(
        Long id,
        VacancyResponse vacancyResponse,
        Set<ApplicationStageResultResponse> applicationStageResultResponse
) {

    public static JobApplicationResponse fromEntity(JobApplication jobApplication) {
        return new JobApplicationResponse(
                jobApplication.getId(),
                VacancyResponse.fromEntity(jobApplication.getVacancy()),
                jobApplication.getApplicationStageResults()
                        .stream()
                        .map(ApplicationStageResultResponse::fromEntity)
                        .collect(Collectors.toSet())
        );
    }
}
