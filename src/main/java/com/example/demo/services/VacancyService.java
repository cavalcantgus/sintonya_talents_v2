package com.example.demo.services;

import com.example.demo.dto.SkillVacancyDTO;
import com.example.demo.dto.VacancyCreateDTO;
import com.example.demo.dto.VacancyResponse;
import com.example.demo.entities.*;
import com.example.demo.enums.*;
import com.example.demo.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VacancyService {

    private final VacancyRepository vacancyRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final VacancyEventPublisher publisher;
    private final SkillBaseRepository skillBaseRepository;
    private final SkillVacancyRepository skillVacancyRepository;
    private final SectorRepository sectorRepository;

    public VacancyService(VacancyRepository vacancyRepository, EnterpriseRepository enterpriseRepository,
                          VacancyEventPublisher publisher,
                          SkillBaseRepository skillBaseRepository,
                          SkillVacancyRepository skillVacancyRepository,
                          SectorRepository sectorRepository) {
        this.vacancyRepository = vacancyRepository;
        this.enterpriseRepository = enterpriseRepository;
        this.publisher = publisher;
        this.skillBaseRepository = skillBaseRepository;
        this.skillVacancyRepository = skillVacancyRepository;
        this.sectorRepository = sectorRepository;
    }

    public List<VacancyResponse> findAll() {
        return vacancyRepository.findAll()
                .stream()
                .map(VacancyResponse::fromEntity)
                .toList();
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
    public Vacancy createVacany(Long enterpriseId, VacancyCreateDTO dto) {

        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));

        Sector sector = sectorRepository.findById(dto.getSectorId())
                .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado"));

        Vacancy vacancy = buildVacancy(dto, enterprise, sector);

        vacancyRepository.save(vacancy);

        createStages(vacancy, dto);

        saveSkills(vacancy, dto.getSkills());

        return vacancy;
    }

    private Vacancy buildVacancy(VacancyCreateDTO dto, Enterprise enterprise, Sector sector) {
        Vacancy vacancy = new Vacancy();

        vacancy.setEnterprise(enterprise);
        vacancy.setSector(sector);
        vacancy.setTechnicalTest(dto.isHasATechnicalTest());
        vacancy.setBehavioralTest(dto.isHasABehavioralTest());
        vacancy.setCertificateRequired(dto.isRequiredCertificate());
        vacancy.setExperienceRange(ExperienceRange.valueOf(dto.getExperienceRange()));
        vacancy.setMinExperienceRange(vacancy.getExperienceRange().getMinMonths());
        vacancy.setMaxExperienceRange(vacancy.getExperienceRange().getMaxMonths());        vacancy.setTitle(dto.getTitle());
        vacancy.setDescription(dto.getDescription());
        vacancy.setPosition(dto.getPosition());
        vacancy.setVacancyType(VacancyType.valueOf(dto.getVacancyType()));
        vacancy.setLocality(dto.getLocality());
        vacancy.setModalityType(WorkModality.valueOf(dto.getModalityType()));
        vacancy.setVacancyStatus(VacancyStatus.PENDING_APPROVAL);

        return vacancy;
    }

    private void createStages(Vacancy vacancy, VacancyCreateDTO dto) {

        if (dto.isHasABehavioralTest()) {
            createStage(vacancy, StageType.BEHAVIORAL_TEST, true);
        }

        if (dto.isHasATechnicalTest()) {
            createStage(vacancy, StageType.TECHNICAL_TEST, true);
        }

        boolean hasRequired = dto.getSkills().stream().anyMatch(SkillVacancyDTO::isRequired);
        boolean hasOptional = dto.getSkills().stream().anyMatch(s -> !s.isRequired());

        if (hasRequired) {
            createStage(vacancy, StageType.SKILL_REQUIRED, true);
        }

        if (hasOptional) {
            createStage(vacancy, StageType.SKILL_OPTIONAL, false);
        }

        createStage(vacancy, StageType.CERTIFICATE, dto.isRequiredCertificate());
    }

    private void saveSkills(Vacancy vacancy, List<SkillVacancyDTO> skills) {

        Map<Long, SkillBase> skillMap = findSkills(skills);

        List<SkillVacancy> entities = skills.stream().map(skill -> {
            SkillBase base = Optional.ofNullable(skillMap.get(skill.getSkillId()))
                    .orElseThrow(() -> new EntityNotFoundException("Habilidade não encontrada"));

            SkillVacancy sv = new SkillVacancy();
            sv.setVacancy(vacancy);
            sv.setSkillBase(base);
            sv.setRequired(skill.isRequired());

            return sv;
        }).toList();

        skillVacancyRepository.saveAll(entities); // 🔥 batch insert
    }

    private Map<Long, SkillBase> findSkills(List<SkillVacancyDTO> skills) {
        List<Long> ids = skills.stream()
                .map(SkillVacancyDTO::getSkillId)
                .toList();

        return skillBaseRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(SkillBase::getId, s -> s));
    }

    private void createStage(Vacancy vacancy, StageType type, boolean isRequiredByCompany) {
        SelectionStage stage = new SelectionStage();

        stage.setVacancy(vacancy);
        stage.setStageType(type);
        stage.setVisible(type.isVisible());
        stage.setStageOrder(type.getDefaultOrder());
        stage.setAnalysisDimension(mapDimension(type));
        stage.setRequired(isRequiredByCompany);
        stage.setWeight(resolveWeight(type, isRequiredByCompany));

        vacancy.getSelectionStages().add(stage);
    }

    private double resolveWeight(StageType type, boolean required) {

        return switch (type) {

            case TECHNICAL_TEST -> required ? 0.25 : 0.10;
            case BEHAVIORAL_TEST -> required ? 0.35 : 0.15;

            case SKILL_REQUIRED -> 0.20;
            case SKILL_OPTIONAL -> 0.10;

            case CERTIFICATE -> required ? 0.10 : 0.02;
            case EXPERIENCE -> required ? 0.10 : 0.05;
        };
    }

    private AnalysisDimension mapDimension(StageType type) {
        return switch (type) {
            case TECHNICAL_TEST -> AnalysisDimension.TECHNICAL;
            case BEHAVIORAL_TEST -> AnalysisDimension.BEHAVIORAL;
            case SKILL_REQUIRED -> AnalysisDimension.REQUIRED_SKILL;
            case SKILL_OPTIONAL -> AnalysisDimension.OPTIONAL_SKILL;
            case CERTIFICATE -> AnalysisDimension.CERTIFICATES;
            case EXPERIENCE -> AnalysisDimension.EXPERIENCES;
        };
    }

    private boolean isVisible(StageType type) {
        return switch (type) {
            case TECHNICAL_TEST, BEHAVIORAL_TEST -> true;
            default -> false;
        };
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

    public int calculateMonths(Experience exp) {
        LocalDate end = exp.isCurrent() ? LocalDate.now() : exp.getEndDate();

        return (int) ChronoUnit.MONTHS.between(exp.getStartDate(), end);
    }
}
