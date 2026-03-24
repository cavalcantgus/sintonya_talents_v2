package com.example.demo.controllers;

import com.example.demo.dto.VacancyCreateDTO;
import com.example.demo.dto.VacancyResponse;
import com.example.demo.entities.Candidate;
import com.example.demo.entities.Vacancy;
import com.example.demo.services.CandidateService;
import com.example.demo.services.VacancyRecommendationService;
import com.example.demo.services.VacancyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/vacancys")
public class VacancyController {

    private final VacancyService vacancyService;
    private final CandidateService candidateService;
    private final VacancyRecommendationService vacancyRecommendationService;

    public VacancyController(VacancyService vacancyService, CandidateService candidateService,
                             VacancyRecommendationService vacancyRecommendationService) {
        this.vacancyService = vacancyService;
        this.candidateService = candidateService;
        this.vacancyRecommendationService = vacancyRecommendationService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VACANCY_VIEW')")
    public ResponseEntity<List<VacancyResponse>>  findAll() {
        List<VacancyResponse> vacancys = vacancyService.findAll();
        return ResponseEntity.ok().body(vacancys);
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<VacancyResponse>> getRecommendations(@AuthenticationPrincipal UserDetails userDetails,
                                                                    @RequestParam(defaultValue = "10") int limit) {
        Candidate candidate = candidateService.findByUserEmail(userDetails.getUsername());
        List<VacancyResponse> recommended = vacancyRecommendationService.recommend(candidate, limit);
        return ResponseEntity.ok().body(recommended);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VACANCY_VIEW') || hasRole('ADMINISTRATOR')")
    public ResponseEntity<Vacancy> findById(@PathVariable Long id) {
        Vacancy vacancy = vacancyService.findById(id);
        return ResponseEntity.ok().body(vacancy);
    }

    @GetMapping("/vacancy-by-enterprise/{enterpriseId}")
    @PreAuthorize("hasAuthority('VACANCY_VIEW') || hasRole('ADMINISTRATOR')")
    public ResponseEntity<List<VacancyResponse>> findByEnterpiseid(@PathVariable Long enterpriseId) {
        List<VacancyResponse> vacancyResponse = vacancyService.findByEnterpriseId(enterpriseId);
        return ResponseEntity.ok().body(vacancyResponse);
    }

    @PostMapping("/{empresaId}/create-vacancy")
//    @PreAuthorize("hasAuthority('VACANCY_CREATE')")
    public ResponseEntity<Vacancy> createVacancy(@RequestBody VacancyCreateDTO objDto, @PathVariable Long empresaId) {
        Vacancy vacancy = vacancyService.createVacany(empresaId, objDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(vacancy.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}/rejected")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Vacancy> rejectVacancy(@PathVariable Long id) {
        Vacancy vacancy = vacancyService.updateIfRejected(id);
        return ResponseEntity.ok().body(vacancy);
    }

    @PutMapping("/{id}/approved")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Vacancy> approveVacancy(@PathVariable Long id) {
        Vacancy vacancy = vacancyService.updateIfApproved(id);
        return ResponseEntity.ok().body(vacancy);
    }
}
