package com.example.demo.services;

import com.example.demo.entities.ApplicationStageResult;
import com.example.demo.entities.JobApplication;
import com.example.demo.repositories.ApplicationAnalysisRepository;
import com.example.demo.repositories.JobApplicationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ApplicationAnalysisService {

    private final ApplicationAnalysisRepository applicationAnalysisRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public ApplicationAnalysisService(ApplicationAnalysisRepository applicationAnalysisRepository,
                                      JobApplicationRepository jobApplicationRepository) {
        this.applicationAnalysisRepository = applicationAnalysisRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    public void save(Long jobApplicationId) {
        JobApplication jobApplication = jobApplicationRepository.findById(jobApplicationId)
                .orElseThrow(() -> new EntityNotFoundException("Candidatura não encontrada"));

        Set<ApplicationStageResult> applicationStageResultList = jobApplication.getApplicationStageResults();

    }
}
