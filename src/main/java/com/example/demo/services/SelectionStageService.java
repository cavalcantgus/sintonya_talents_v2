package com.example.demo.services;

import com.example.demo.dto.SelectionStageCreateDTO;
import com.example.demo.entities.SelectionStage;
import com.example.demo.entities.Vacancy;
import com.example.demo.enums.StageType;
import com.example.demo.repositories.SelectionStageRepository;
import com.example.demo.repositories.VacancyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
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

    @Transactional
    public void update(Long vacancyId, SelectionStageCreateDTO objDto) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));

        SelectionStage selectionStage = vacancy.getSelectionStages()
                .stream()
                .filter(s -> s.getStageType().toString().equals(objDto.getStageType()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Etapa não encontrada"));


        selectionStage.setUrl(objDto.getUrl());
        selectionStage.setMaxScore(objDto.getMaxScore());
        if(objDto.getStageType().equals("BEHAVIORAL_TESTE")) {
            selectionStage.setName("Teste Comportamental");
        } else {
            selectionStage.setName("Teste Técnico");
        }

    }

    private void addSkillSelectionStage(Vacancy vacancy) {
        SelectionStage selectionStage = new SelectionStage();
        selectionStage.setName("Teste de Habilidade");
        selectionStage.setVacancy(vacancy);
        selectionStage.setStageType(StageType.SKILL_OPTIONAL);
        selectionStage.setVisible(false);
        vacancy.getSelectionStages().add(selectionStage);
    }

    private void addCertificateSelectionStage(Vacancy vacancy) {
        SelectionStage selectionStage = new SelectionStage();
        selectionStage.setName("Teste de Certificado");
        selectionStage.setVacancy(vacancy);
        selectionStage.setStageType(StageType.CERTIFICATE);
        selectionStage.setVisible(false);
        vacancy.getSelectionStages().add(selectionStage);

    }

    private void addExperienceSelectionStage(Vacancy vacancy) {
        SelectionStage selectionStage = new SelectionStage();
        selectionStage.setName("Teste de Experiência");
        selectionStage.setVacancy(vacancy);
        selectionStage.setStageType(StageType.EXPERIENCE);
        selectionStage.setVisible(false);
        vacancy.getSelectionStages().add(selectionStage);

    }
}
