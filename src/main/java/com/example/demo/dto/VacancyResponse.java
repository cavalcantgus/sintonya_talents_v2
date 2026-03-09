package com.example.demo.dto;

import com.example.demo.entities.Enterprise;
import com.example.demo.entities.Vacancy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record VacancyResponse(
      String title,
      String description,
      String position,
      String locality,
      LocalDate publicationDate,
      String modalityType,
      String vacancyStatus,
      String vacancyType,
      LocalDate expirationDate,
      LocalDateTime closedAt,
      EnterpriseResponse enterpriseResponse,
      Set<SelectionStageResponse> selectionStagesResponse
) {
   public static VacancyResponse fromEntity(Vacancy vacancy) {
       return new VacancyResponse(
               vacancy.getTitle(),
               vacancy.getDescription(),
               vacancy.getPosition(),
               vacancy.getLocality(),
               vacancy.getPublicationDate(),
               vacancy.getModalityType().toString(),
               vacancy.getVacancyStatus().toString(),
               vacancy.getVacancyType().toString(),
               vacancy.getExpirationDate(),
               vacancy.getClosedAt(),
               EnterpriseResponse.fromEntity(vacancy.getEnterprise()),
               vacancy.getSelectionStages()
                       .stream()
                       .map(SelectionStageResponse::fromEntity)
                       .collect(Collectors.toSet())
       );
   }
}
