package com.example.demo.controllers;

import com.example.demo.dto.JobApplicationResponse;
import com.example.demo.entities.JobApplication;
import com.example.demo.services.JobApplicationService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job-application")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping()
    public ResponseEntity<List<JobApplicationResponse>> findAll() {
        List<JobApplicationResponse> jobApplicationResponses = jobApplicationService.findAll();
        return ResponseEntity.ok().body(jobApplicationResponses);
    }
    @PostMapping("/{candidateId}/apply/{vacancyId}")
    public ResponseEntity<?> apply(@PathVariable Long candidateId, @PathVariable Long vacancyId) {
        jobApplicationService.apply(candidateId, vacancyId);
        return ResponseEntity.ok().build();
    }
}
