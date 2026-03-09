package com.example.demo.services;

import com.example.demo.dto.SelectionStageCreateDTO;
import com.example.demo.entities.SelectionStage;
import com.example.demo.entities.Vacancy;
import com.example.demo.enums.StageType;
import com.example.demo.repositories.SelectionStageRepository;
import com.example.demo.repositories.VacancyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelectionStageService {

    private final SelectionStageRepository selectionStageRepository;
    private final VacancyRepository vacancyRepository;

    public SelectionStageService(SelectionStageRepository selectionStageRepository,
                                 VacancyRepository vacancyRepository) {
        this.selectionStageRepository = selectionStageRepository;
        this.vacancyRepository = vacancyRepository;
    }

    public void insert(Long vacancyId, List<SelectionStageCreateDTO> objDto) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));

        for (int i = 0; i < objDto.size(); i++) {
            SelectionStage selectionStage = new SelectionStage();
            selectionStage.setVacancy(vacancy);
            selectionStage.setStageType(StageType.valueOf(objDto.get(i).getStageType()));
            selectionStage.setUrl(objDto.get(i).getUrl());
            selectionStage.setStageOrder(i);

            if(objDto.get(i).getStageType().toString().equals("BEHAVIORAL_TEST")) {
                selectionStage.setName("Teste Comportamental");
            } else {
                selectionStage.setName("Teste Técnico");
            }

            selectionStageRepository.save(selectionStage);
        }

    }

}
