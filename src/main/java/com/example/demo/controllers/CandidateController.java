package com.example.demo.controllers;

import com.example.demo.dto.CandidateResponse;
import com.example.demo.dto.CandidateUpdateDTO;
import com.example.demo.entities.Candidate;
import com.example.demo.services.CandidateService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    public ResponseEntity<List<Candidate>> findAll() {
        List<Candidate> candidates = candidateService.findAll();
        return ResponseEntity.ok().body(candidates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateResponse> findById(@PathVariable Long id) {
        CandidateResponse candidate = candidateService.findById(id);
        return ResponseEntity.ok().body(candidate);
    }

    @GetMapping("/findCandidateByUser/{userId}")
    public ResponseEntity<CandidateResponse> findByUserId(@PathVariable Long userId) {
        CandidateResponse candidate = candidateService.findById(userId);
        return ResponseEntity.ok().body(candidate);
    }

    @PutMapping("/update-personal-infos/candidate/{id}")
    public ResponseEntity<CandidateResponse> updatePersonalInfos(@PathVariable Long id, @RequestBody CandidateUpdateDTO objDto) {
        CandidateResponse candidate = candidateService.update(id, objDto);
        return ResponseEntity.ok().body(candidate);
    }

    @PutMapping("/update-personal-summary/candidate/{id}")
    public ResponseEntity<CandidateResponse> updatePersonalSummary(@PathVariable Long id, @RequestBody String personalSummary) {
        CandidateResponse candidateResponse = candidateService.update(id, personalSummary);
        return ResponseEntity.ok().body(candidateResponse);
    }

    @PutMapping("/upadate/add-habilities/candidate/{id}")
    public ResponseEntity<CandidateResponse> addHabilities(@PathVariable Long id) {
        return null;
    }


}

