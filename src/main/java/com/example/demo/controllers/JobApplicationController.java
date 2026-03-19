package com.example.demo.controllers;

import com.example.demo.dto.FinishTest;
import com.example.demo.dto.JobApplicationResponse;
import com.example.demo.entities.JobApplication;
import com.example.demo.services.JobApplicationService;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/job-applications")
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

    @GetMapping("/by-candidate/{candidateId}")
    public ResponseEntity<List<JobApplicationResponse>> findAllByCandidateId(@PathVariable Long candidateId) {
        List<JobApplicationResponse> jobApplicationResponses = jobApplicationService.findAllByCandidateId(candidateId);
        return ResponseEntity.ok().body(jobApplicationResponses);
    }

    @PostMapping("/{candidateId}/apply/{vacancyId}")
    public ResponseEntity<?> apply(@PathVariable Long candidateId, @PathVariable Long vacancyId) {
        jobApplicationService.apply(candidateId, vacancyId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/teste/{jobApplicationId}/{applicationStageResultId}")
    public ResponseEntity<?> startTest(@PathVariable Long jobApplicationId, @PathVariable Long applicationStageResultId) {
        jobApplicationService.startTest(jobApplicationId, applicationStageResultId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/finish-test")
    public ResponseEntity<Map<String, String>> finishTest(@RequestBody FinishTest dto) {
        String fullName = jobApplicationService.finishTest(dto);
        return ResponseEntity.ok(Map.of("fullName", fullName));
    }

    @PostMapping("/generate-analysis/{jobApplicationId}")
    public ResponseEntity<Void> generateAnalysis(@PathVariable Long jobApplicationId) {
        jobApplicationService.generateAnalysis(jobApplicationId);
        return ResponseEntity.ok().build();
    }
}
