package com.example.demo.dto;

import com.example.demo.entities.Certificates;
import com.example.demo.entities.Experience;

import java.time.LocalDate;

public record ExperienceResponse(
        Long id,
        String title,
        String workType,
        String organization,
        String locality,
        LocalDate startDate,
        LocalDate endDate,
        boolean isCurrent,
        String workModality,
        String description,
        CandidateResponse candidate
) {
    public static ExperienceResponse fromEntity(Experience experience) {
        return new ExperienceResponse(
                experience.getId(),
                experience.getTitle(),
                experience.getWorkType().toString(),
                experience.getOrganization(),
                experience.getLocality(),
                experience.getStartDate(),
                experience.getEndDate(),
                experience.isCurrent(),
                experience.getWorkModality().toString(),
                experience.getDescription(),
                CandidateResponse.fromEntity(experience.getCandidate())
        );
    }
}
