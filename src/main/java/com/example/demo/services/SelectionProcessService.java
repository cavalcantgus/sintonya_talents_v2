package com.example.demo.services;

import com.example.demo.dto.SelectionProcessCreateDTO;
import com.example.demo.dto.SelectionProcessResponse;
import com.example.demo.entities.SelectionProcess;
import com.example.demo.entities.Vacancy;
import com.example.demo.repositories.SelectionProcessRepository;
import com.example.demo.repositories.VacancyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelectionProcessService {

    private final SelectionProcessRepository selectionProcessRepository;
    private final VacancyRepository vacancyRepository;

    public SelectionProcessService(SelectionProcessRepository selectionProcessRepository,
                                   VacancyRepository vacancyRepository) {
        this.selectionProcessRepository = selectionProcessRepository;
        this.vacancyRepository = vacancyRepository;
    }

    public List<SelectionProcess> findAll() {
        return selectionProcessRepository.findAll();
    }

    public SelectionProcess findById(Long id) {
        return selectionProcessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Processo Seletivo não encontrado"));
    }

    public SelectionProcessResponse insert(Long vacancyId, SelectionProcessCreateDTO objDto) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));

        SelectionProcess selectionProcess = new SelectionProcess();
        selectionProcess.setTitle(objDto.getTitle());
        selectionProcess.setDescription(objDto.getDescription());
        selectionProcess.setVacancy(vacancy);

        selectionProcessRepository.save(selectionProcess);

        return SelectionProcessResponse.fromEntity(selectionProcess);
    }
}
