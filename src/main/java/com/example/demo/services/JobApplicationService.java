package com.example.demo.services;

import com.example.demo.dto.FinishTest;
import com.example.demo.dto.JobApplicationResponse;
import com.example.demo.entities.*;
import com.example.demo.enums.ApplicationStatus;
import com.example.demo.enums.StageStatus;
import com.example.demo.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final CandidateRepository candidateRepository;
    private final VacancyRepository vacancyRepository;
    private final SelectionStageRepository selectionStageRepository;
    private final ApplicationStageResultRepository applicationStageResultRepository;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository,
                                 CandidateRepository candidateRepository,
                                 VacancyRepository vacancyRepository,
                                 SelectionStageRepository selectionStageRepository,
                                 ApplicationStageResultRepository applicationStageResultRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.candidateRepository = candidateRepository;
        this.vacancyRepository = vacancyRepository;
        this.selectionStageRepository = selectionStageRepository;
        this.applicationStageResultRepository = applicationStageResultRepository;
    }

    public List<JobApplicationResponse> findAllByCandidateId(Long candidateId) {
        return jobApplicationRepository.findAllByCandidateId(candidateId)
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
            if (selectionStage.getStageOrder() == 0) {
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

}
