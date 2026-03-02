package com.example.demo.dto;

import com.example.demo.entities.Vacancy;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
      LocalDateTime closedAt

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
               vacancy.getClosedAt()
       );
   }
}
