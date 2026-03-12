package com.example.demo.services;

import com.example.demo.dto.JobApplicationResponse;
import com.example.demo.entities.*;
import com.example.demo.enums.ApplicationStatus;
import com.example.demo.enums.StageStatus;
import com.example.demo.repositories.CandidateRepository;
import com.example.demo.repositories.JobApplicationRepository;
import com.example.demo.repositories.SelectionStageRepository;
import com.example.demo.repositories.VacancyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final CandidateRepository candidateRepository;
    private final VacancyRepository vacancyRepository;
    private final SelectionStageRepository selectionStageRepository;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository,
                                 CandidateRepository candidateRepository,
                                 VacancyRepository vacancyRepository,
                                 SelectionStageRepository selectionStageRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.candidateRepository = candidateRepository;
        this.vacancyRepository = vacancyRepository;
        this.selectionStageRepository = selectionStageRepository;
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
}
