package com.example.demo.repositories;

import com.example.demo.entities.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findAllByCandidateId(Long candidateId);
    JobApplication findByCandidateIdAndVacancyId(Long candidateId, Long vacancyId);
    List<JobApplication> findAllByVacancyId(Long vacancyId);
}
