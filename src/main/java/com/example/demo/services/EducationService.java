package com.example.demo.services;

import com.example.demo.dto.EducationCreateDTO;
import com.example.demo.dto.EducationResponse;
import com.example.demo.dto.EducationUpdateDTO;
import com.example.demo.entities.*;
import com.example.demo.enums.SkillLevel;
import com.example.demo.enums.SkillSource;
import com.example.demo.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EducationService {

    private final EducationRepository educationRepository;
    private final InstitutionRepository institutionRepository;
    private final CandidateRepository candidateRepository;
    private final StudyAreaRepository studyAreaRepository;
    private final SkillBaseRepository skillBaseRepository;

    public EducationService(EducationRepository educationRepository, InstitutionRepository institutionRepository,
                            CandidateRepository candidateRepository,
                            StudyAreaRepository studyAreaRepository,
                            SkillBaseRepository skillBaseRepository) {
        this.educationRepository = educationRepository;
        this.institutionRepository = institutionRepository;
        this.candidateRepository = candidateRepository;
        this.studyAreaRepository = studyAreaRepository;
        this.skillBaseRepository = skillBaseRepository;
    }

    public List<EducationResponse> findAll() {
        return educationRepository.findAll()
                .stream()
                .map(EducationResponse::fromEntity)
                .toList();
    }

    public EducationResponse findById(Long id) {
        Education education = educationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formação não encontrada"));

        return EducationResponse.fromEntity(education);
    }

    @Transactional
    public EducationResponse insert(EducationCreateDTO objDto, Long id) {
        Education education = new Education();

        StudyArea studyArea = studyAreaRepository.findById(objDto.getStudyArea())
                .orElseThrow(() -> new EntityNotFoundException("Área de estudo não encontrada"));

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrdo"));

        Institution institution = institutionRepository.findById(objDto.getEducationalInstitutionId())
                .orElseThrow(() -> new EntityNotFoundException("Instituição não encontrada"));

        education.setEducationalInstitution(institution);
        education.setCandidate(candidate);
        education.setDescription(objDto.getDescription());
        education.setDiploma(objDto.getDiploma());
        education.setStudyArea(studyArea);
        education.setStartDate(buildDate(objDto.getStartMonth(), objDto.getStartYear()));
        education.setEndDate(buildDate(objDto.getEndMonth(), objDto.getEndYear()));

        Set<SkillBase> skills = new HashSet<>();
        if (objDto.getEducationSkills() != null && !objDto.getEducationSkills().isEmpty()) {
            skills.addAll(skillBaseRepository.findAllById(objDto.getEducationSkills()));
        }

        for (SkillBase skill : skills) {
            SkillCandidate skillCandidate = new SkillCandidate();
            skillCandidate.setCandidate(candidate);
            skillCandidate.setSkillBase(skill);
            skillCandidate.setSource(SkillSource.EDUCATION);
            skillCandidate.setEducation(education);
            skillCandidate.setSkillLevel(SkillLevel.NON_SPECIFIED);

            candidate.getCandidateSkills().add(skillCandidate);
            education.getEducationSkills().add(skillCandidate);
        }

        educationRepository.save(education);
        return EducationResponse.fromEntity(education);
    }

    @Transactional
    public EducationResponse update(EducationUpdateDTO objDto, Long id) {
        Education education = educationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formação não encontrada"));

        Institution institution = institutionRepository.findById(objDto.getEducationalInstitutionId())
                .orElseThrow(() -> new EntityNotFoundException("Instituição não encontrada"));

        StudyArea studyArea = studyAreaRepository.findById(objDto.getStudyArea())
                        .orElseThrow(() -> new EntityNotFoundException("Área de estudo não encontrada"));

        education.setEducationalInstitution(institution);
        education.setDiploma(objDto.getDiploma());
        education.setDescription(objDto.getDescription());
        education.setStudyArea(studyArea);
        education.setStartDate(buildDate(objDto.getStartMonth(), objDto.getStartYear()));
        education.setEndDate(buildDate(objDto.getEndMonth(), objDto.getEndYear()));

        educationRepository.save(education);
        updateSkills(education, objDto);
        return EducationResponse.fromEntity(education);
    }

    private LocalDate buildDate(String month, String year) {
        if (month == null || year == null || month.isBlank() || year.isBlank()) {
            return null;
        }
        return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);
    }

    private void updateSkills(Education education, EducationUpdateDTO objDto) {
        Set<SkillCandidate> current = education.getEducationSkills();

        Set<Long> newIds = new HashSet<>(objDto.getEducationSkills());

        Set<Long> currentIds = current.stream()
                .map(sc -> sc.getSkillBase().getId())
                .collect(Collectors.toSet());

        current.removeIf(sc -> !newIds.contains(sc.getSkillBase().getId()));

        for (Long skillId : newIds) {
            if (!currentIds.contains(skillId)) {

                SkillBase skillBase = skillBaseRepository.findById(skillId)
                        .orElseThrow(() -> new EntityNotFoundException("Skill não encontrada"));

                SkillCandidate sc = new SkillCandidate();
                sc.setSkillBase(skillBase);
                sc.setEducation(education);
                sc.setCandidate(education.getCandidate());
                sc.setSource(SkillSource.EDUCATION);
                current.add(sc);
            }
        }
    }

    public void delete(Long id) {
        Education education = educationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formação não encontrada"));

        educationRepository.delete(education);
    }
}
