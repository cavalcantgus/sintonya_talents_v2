package com.example.demo.dto;

import com.example.demo.entities.Education;
import com.example.demo.entities.Institution;

import java.time.LocalDate;

public record EducationResponse(
        Long id,
        Institution educationalInstitution,
        String diploma,
        String description,
        LocalDate startDate,
        LocalDate endDate
) {
    public static EducationResponse fromEntity(Education education) {
        return new EducationResponse(
                education.getId(),
                education.getEducationalInstitution(),
                education.getDiploma(),
                education.getDescription(),
                education.getStartDate(),
                education.getEndDate()
        );
    }
}
