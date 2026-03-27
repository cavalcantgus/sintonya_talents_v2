package com.example.demo.services;

import com.example.demo.dto.ExperienceCreateDTO;
import com.example.demo.dto.ExperienceResponse;
import com.example.demo.dto.VacancyResponse;
import com.example.demo.entities.*;
import com.example.demo.enums.SkillLevel;
import com.example.demo.enums.SkillSource;
import com.example.demo.enums.VacancyType;
import com.example.demo.enums.WorkModality;
import com.example.demo.repositories.CandidateRepository;
import com.example.demo.repositories.ExperienceRepository;
import com.example.demo.repositories.SkillBaseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final CandidateRepository candidateRepository;
    private final SkillBaseRepository skillBaseRepository;

    public ExperienceService(ExperienceRepository experienceRepository, CandidateRepository candidateRepository,
                             SkillBaseRepository skillBaseRepository) {
        this.experienceRepository = experienceRepository;
        this.candidateRepository = candidateRepository;
        this.skillBaseRepository = skillBaseRepository;
    }

    public List<ExperienceResponse> findAll() {
        return experienceRepository.findAll()
                .stream()
                .map(ExperienceResponse::fromEntity)
                .toList();
    }
    public ExperienceResponse findById(Long id) {
        Experience experience = experienceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Experiência não encontrada"));
        return ExperienceResponse.fromEntity(experience);
    }

    @Transactional
    public ExperienceResponse insert(Long candidateId, ExperienceCreateDTO objDto) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));
        CandidatePreferences candidatePreferences = candidate.getPreferences();

        System.out.println("IS CURRENT? " + objDto.isCurrent());
        if (objDto.isCurrent() && objDto.getEndDate() != null) {
            throw new IllegalArgumentException("Experiência atual não deve possuir data de fim");
        }

        if (!objDto.isCurrent() && objDto.getEndDate() == null) {
            throw new IllegalArgumentException("Experiência não atual deve possuir data de fim");
        }

        Experience experience = new Experience();
        experience.setTitle(objDto.getTitle());
        experience.setWorkType(VacancyType.valueOf(objDto.getWorkType()));
        experience.setOrganization(objDto.getOrganization());
        experience.setStartDate(objDto.getStartDate());
        experience.setEndDate(objDto.getEndDate());
        experience.setCurrent(objDto.isCurrent());
        experience.setWorkModality(WorkModality.valueOf(objDto.getWorkModality()));
        experience.setDescription(objDto.getDescription());
        experience.setCandidate(candidate);

        long months = ChronoUnit.MONTHS.between(
                objDto.getStartDate(),
                objDto.isCurrent() ? LocalDate.now() : objDto.getEndDate()
        );

        candidatePreferences.setTotalExperience(candidatePreferences.getTotalExperience() + months);
        candidate.getExperiences().add(experience);
        candidate.setPreferences(candidatePreferences);
        Set<SkillBase> skills = new HashSet<>(
                skillBaseRepository.findAllById(objDto.getSkillsId())
        );

        for (SkillBase skill : skills) {
            SkillCandidate skillCandidate = new SkillCandidate();
            skillCandidate.setCandidate(candidate);
            skillCandidate.setSkillBase(skill);
            skillCandidate.setSource(SkillSource.EXPERIENCE);
            skillCandidate.setExperience(experience);
            skillCandidate.setSkillLevel(SkillLevel.NON_SPECIFIED);

            candidate.getCandidateSkills().add(skillCandidate);

            experience.getExperienceSkills().add(skillCandidate);
        }

        candidateRepository.save(candidate);

        return ExperienceResponse.fromEntity(experience);
    }
}
