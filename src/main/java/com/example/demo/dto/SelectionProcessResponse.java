package com.example.demo.dto;


import com.example.demo.entities.SelectionProcess;

public record SelectionProcessResponse(
        Long id,
        String title,
        String description,
        boolean isActive,
        VacancyResponse vacancy
) {

public static SelectionProcessResponse fromEntity(SelectionProcess selectionProcess) {
    return new SelectionProcessResponse(
            selectionProcess.getId(),
            selectionProcess.getTitle(),
            selectionProcess.getDescription(),
            selectionProcess.isActive(),
            VacancyResponse.fromEntity(selectionProcess.getVacancy())
    );
}
}
