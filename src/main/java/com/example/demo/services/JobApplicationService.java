package com.example.demo.services;

import com.example.demo.dto.FinishTest;
import com.example.demo.dto.JobApplicationResponse;
import com.example.demo.entities.*;
import com.example.demo.enums.AnalysisDimension;
import com.example.demo.enums.ApplicationStatus;
import com.example.demo.enums.StageStatus;
import com.example.demo.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final CandidateRepository candidateRepository;
    private final VacancyRepository vacancyRepository;
    private final SelectionStageRepository selectionStageRepository;
    private final ApplicationStageResultRepository applicationStageResultRepository;
    private final ApplicationAnalysisRepository applicationAnalysisRepository;
    private final SkillVacancyRepository skillVacancyRepository;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository,
                                 CandidateRepository candidateRepository,
                                 VacancyRepository vacancyRepository,
                                 SelectionStageRepository selectionStageRepository,
                                 ApplicationStageResultRepository applicationStageResultRepository,
                                 ApplicationAnalysisRepository applicationAnalysisRepository,
                                 SkillVacancyRepository skillVacancyRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.candidateRepository = candidateRepository;
        this.vacancyRepository = vacancyRepository;
        this.selectionStageRepository = selectionStageRepository;
        this.applicationStageResultRepository = applicationStageResultRepository;
        this.applicationAnalysisRepository = applicationAnalysisRepository;
        this.skillVacancyRepository = skillVacancyRepository;
    }

    public List<JobApplicationResponse> findAllByCandidateId(Long candidateId) {
        return jobApplicationRepository.findAllByCandidateId(candidateId)
                .stream()
                .map(JobApplicationResponse::fromEntity)
                .toList();
    }

    public List<JobApplicationResponse> findAllByVacancyId(Long vacancyId) {
        return jobApplicationRepository.findAllByVacancyId(vacancyId)
                .stream()
                .map(JobApplicationResponse::fromEntity)
                .toList();
    }

    public List<JobApplicationResponse> findAll() {
        return jobApplicationRepository.findAll()
                .stream()
                .map(JobApplicationResponse::fromEntity)
                .toList();
    }

    @Transactional
    public void apply(Long candidateId, Long vacancyId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));

        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));

        List<SelectionStage> selectionStages = selectionStageRepository.findByVacancyId(vacancyId);

        JobApplication jobApplication = new JobApplication();
        jobApplication.setCandidate(candidate);
        jobApplication.setVacancy(vacancy);
        jobApplication.setStatus(ApplicationStatus.WAITING_TEST);

        for(SelectionStage selectionStage : selectionStages) {
            ApplicationStageResult applicationStageResult = new ApplicationStageResult();
            applicationStageResult.setJobApplication(jobApplication);
            applicationStageResult.setSelectionStage(selectionStage);
            applicationStageResult.setScore(0.0);
            applicationStageResult.setStageStatus(StageStatus.NOT_STARTED);
            if (selectionStage.getStageOrder() != null && selectionStage.getStageOrder() == 1) {
                applicationStageResult.setLocked(false);
            }

            jobApplication.getApplicationStageResults().add(applicationStageResult);
        }

        jobApplicationRepository.save(jobApplication);
    }

    @Transactional
    public void startTest(Long jobApplicationId, Long applicationStageResultId) {
        applicationStageResultRepository.lockOtherStages(jobApplicationId, applicationStageResultId);
        applicationStageResultRepository.startStage(applicationStageResultId);
    }

    @Transactional
    public String finishTest(FinishTest dto) {
        ApplicationStageResult result =
                applicationStageResultRepository.findStageResult(dto.getCpf(), dto.getUrl())
                        .orElseThrow(() -> new EntityNotFoundException("Resultado não encontrado"));

        result.setStageStatus(StageStatus.COMPLETED);
        result.setLocked(true);
        result.setScore(dto.getScore());
        return result.toString();
    }

    @Transactional
    public void generateAnalysis(Long jobApplicationId) {
        JobApplication jobApplication = jobApplicationRepository.findById(jobApplicationId)
                .orElseThrow(() -> new EntityNotFoundException("Candidatura não encontrada"));

        ApplicationAnalysis applicationAnalysis = new ApplicationAnalysis();
        applicationAnalysis.setJobApplication(jobApplication);

        Double finalScore = 0.0;

        for(ApplicationStageResult applicationStageResult : jobApplication.getApplicationStageResults()) {
            SelectionStage stage = applicationStageResult.getSelectionStage();

            double weight = stage.getWeight();

            double score;

            if (stage.getAnalysisDimension() == AnalysisDimension.TECHNICAL ||
            stage.getAnalysisDimension() == AnalysisDimension.BEHAVIORAL) {

                score = applicationStageResult.getScore();

                score = stage.getMaxScore() != null
                        ? score / stage.getMaxScore()
                        : score;

            } else {
                score = resolveScore(jobApplication.getCandidate(), stage);
            }

            double weighted = score * weight;

            ApplicationAnalysisDimension dim = new ApplicationAnalysisDimension();
            dim.setAnalysis(applicationAnalysis);
            dim.setSelectionStage(stage);
            dim.setScore(score);
            dim.setWeight(weight);
            dim.setWeightedScore(weighted);

            finalScore += weighted;

            applicationAnalysis.getApplicationAnalysesDimensions().add(dim);
        }

        applicationAnalysis.setFinalScore(finalScore);
//        applicationAnalysis.setCompatibilityLevel(calculateLevel(finalScore));

        applicationAnalysisRepository.save(applicationAnalysis);
    }

    private double resolveScore(Candidate candidate, SelectionStage stage) {

        return switch (stage.getAnalysisDimension()) {

            case REQUIRED_SKILL -> calculateRequiredSkillScore(candidate, stage);

            case OPTIONAL_SKILL -> calculateOptionalSkillScore(candidate, stage);

            case CERTIFICATES -> calculateCertificateScore(candidate, stage);

//            case EXPERIENCES -> calculateExperienceScore(candidate, stage);

            default -> 0.0; // TECHNICAL / BEHAVIORAL vem do StageResult
        };
    }

    private double calculateRequiredSkillScore(Candidate candidate, SelectionStage stage) {

        Set<Long> requiredSkills = skillVacancyRepository
                .findRequiredSkillIdsByVacancy(stage.getVacancy().getId());

        Set<Long> candidateSkills = candidate.getCandidateSkills()
                .stream()
                .map(s -> s.getSkillBase().getId())
                .collect(Collectors.toSet());

        long match = requiredSkills.stream()
                .filter(candidateSkills::contains)
                .count();

        if(requiredSkills.isEmpty()) return 1.0;

        System.out.println("RESULTADO DA ETAPA: " + stage.getStageType().toString() + " = " + (double) match / requiredSkills.size());
        return (double) match / requiredSkills.size();
    }

    private double calculateOptionalSkillScore(Candidate candidate, SelectionStage stage) {

        Set<Long> optionalSkills = skillVacancyRepository
                .findNonRequiredSkillIdsByVacancy(stage.getVacancy().getId());

        Set<Long> candidateSkills = candidate.getCandidateSkills()
                .stream()
                .map(s -> s.getSkillBase().getId())
                .collect(Collectors.toSet());

        long match = optionalSkills.stream()
                .filter(candidateSkills::contains)
                .count();

        if(optionalSkills.isEmpty()) return 0.0;

        System.out.println("RESULTADO DA ETAPA: " + stage.getStageType().toString() + " = " + (double) match / optionalSkills.size());

        return (double) match / optionalSkills.size();
    }

    private double calculateCertificateScore(Candidate candidate, SelectionStage stage) {
        Sector vacancySector = stage.getVacancy().getSector();

        long total = candidate.getCertificates().size();

        if (total == 0) return 0.0;

        long match = candidate.getCertificates().stream()
                .filter(c -> c.getSectors().stream()
                        .anyMatch(s -> s.getId().equals(vacancySector.getId())))
                .count();

        System.out.println("RESULTADO DA ETAPA: " + stage.getStageType().toString() + " = " + (double) match / total);

        return (double) match / total;
    }

//    private double calculateExperienceScore(Candidate candidate, SelectionStage stage) {
//        return null;
//    }
}
