package com.example.demo.services;

import com.example.demo.dto.VacancyCreateDTO;
import com.example.demo.dto.VacancyResponse;
import com.example.demo.entities.Enterprise;
import com.example.demo.entities.SkillBase;
import com.example.demo.entities.SkillVacancy;
import com.example.demo.entities.Vacancy;
import com.example.demo.enums.VacancyStatus;
import com.example.demo.enums.VacancyType;
import com.example.demo.enums.WorkModality;
import com.example.demo.repositories.EnterpriseRepository;
import com.example.demo.repositories.SkillBaseRepository;
import com.example.demo.repositories.SkillVacancyRepository;
import com.example.demo.repositories.VacancyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class VacancyService {

    private final VacancyRepository vacancyRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final VacancyEventPublisher publisher;
    private final SkillBaseRepository skillBaseRepository;
    private final SkillVacancyRepository skillVacancyRepository;

    public VacancyService(VacancyRepository vacancyRepository, EnterpriseRepository enterpriseRepository,
                          VacancyEventPublisher publisher,
                          SkillBaseRepository skillBaseRepository,
                          SkillVacancyRepository skillVacancyRepository) {
        this.vacancyRepository = vacancyRepository;
        this.enterpriseRepository = enterpriseRepository;
        this.publisher = publisher;
        this.skillBaseRepository = skillBaseRepository;
        this.skillVacancyRepository = skillVacancyRepository;
    }

    public List<Vacancy> findAll() {
        return vacancyRepository.findAll();
    }

    public List<VacancyResponse> findByEnterpriseId(Long id) {
        return vacancyRepository.findByEnterpriseId(id)
                .stream()
                .map(VacancyResponse::fromEntity)
                .toList();
    }

    public Vacancy findById(Long id) {
        return vacancyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));
    }


    @Transactional
    public Vacancy createVacany(Long enterpriseId, VacancyCreateDTO objDto) {
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));

        Vacancy vacancy = new Vacancy();
        vacancy.setEnterprise(enterprise);
        vacancy.setTitle(objDto.getTitle());
        vacancy.setDescription(objDto.getDescription());
        vacancy.setPosition(objDto.getPosition());
        vacancy.setVacancyType(VacancyType.valueOf(objDto.getVacancyType()));
        vacancy.setLocality(objDto.getLocality());
        vacancy.setModalityType(WorkModality.valueOf(objDto.getModalityType()));
        vacancy.setVacancyStatus(VacancyStatus.PENDING_APPROVAL);

        vacancyRepository.save(vacancy);

        for(Long id : objDto.getSkillsIds()) {
            SkillBase skillBase = skillBaseRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Habilidade não encontrada"));

            SkillVacancy skillVacancy = new SkillVacancy();
            skillVacancy.setVacancy(vacancy);
            skillVacancy.setSkillBase(skillBase);
            skillVacancyRepository.save(skillVacancy);

        }
//        publisher.publishVacancyCreated(vacancy);
        return vacancy;
    }

    @Transactional
    public Vacancy updateIfRejected(Long id) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));

        vacancy.setVacancyStatus(VacancyStatus.REJECTED);
        vacancy.setClosedAt(LocalDateTime.now());

        return vacancyRepository.save(vacancy);
    }

    @Transactional
    public Vacancy updateIfApproved(Long id) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));

        vacancy.setVacancyStatus(VacancyStatus.APPROVED);
        vacancy.setPublicationDate(LocalDate.now());

        return vacancyRepository.save(vacancy);
    }
}
