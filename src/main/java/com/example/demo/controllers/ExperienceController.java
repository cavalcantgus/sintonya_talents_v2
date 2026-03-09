package com.example.demo.controllers;

import com.example.demo.dto.ExperienceCreateDTO;
import com.example.demo.dto.ExperienceResponse;
import com.example.demo.services.ExperienceService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/experiences")
public class ExperienceController {

    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ExperienceResponse>> findAll() {
        List<ExperienceResponse> experiences = experienceService.findAll();
        return ResponseEntity.ok().body(experiences);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienceResponse> findById(@PathVariable Long id) {
        ExperienceResponse experience = experienceService.findById(id);
        return ResponseEntity.ok().body(experience);
    }

    @PostMapping("/{candidateId}/add-experience")
    @PreAuthorize("hasAuthority('EXPERIENCE_CREATE')")
    public ResponseEntity<ExperienceResponse> insert(@PathVariable Long candidateId, @RequestBody ExperienceCreateDTO objDto) {
        ExperienceResponse experience = experienceService.insert(candidateId, objDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(experience.id()).toUri();
        return ResponseEntity.created(uri).body(experience);
    }
}
