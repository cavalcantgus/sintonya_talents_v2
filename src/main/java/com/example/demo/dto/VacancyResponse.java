package com.example.demo.dto;

import com.example.demo.entities.Vacancy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record VacancyResponse(
        Long vacancyId,
      Long postId,
      String title,
      String description,
      String position,
      String locality,
      Long sectorId,
      String modalityType,
      String vacancyStatus,
      String vacancyType,
      LocalDate expirationDate,
      EnterpriseResponse enterpriseResponse,
      Set<SelectionStageResponse> selectionStagesResponse
) {
   public static VacancyResponse fromEntity(Vacancy vacancy) {
       return new VacancyResponse(
               vacancy.getId(),
               vacancy.getPost().getId(),
               vacancy.getTitle(),
               vacancy.getDescription(),
               vacancy.getPosition(),
               vacancy.getLocality(),
               vacancy.getSector().getId(),
               vacancy.getModalityType().toString(),
               vacancy.getPost().getStatus().toString(),
               vacancy.getVacancyType().toString(),
               vacancy.getExpirationDate(),
               EnterpriseResponse.fromEntity(vacancy.getEnterprise()),
               vacancy.getSelectionStages()
                       .stream()
                       .map(SelectionStageResponse::fromEntity)
                       .collect(Collectors.toSet())
       );
   }
}
