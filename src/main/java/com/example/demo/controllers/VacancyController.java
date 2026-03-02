package com.example.demo.controllers;

import com.example.demo.dto.VacancyCreateDTO;
import com.example.demo.dto.VacancyResponse;
import com.example.demo.entities.Vacancy;
import com.example.demo.services.VacancyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/vacancys")
public class VacancyController {

    private final VacancyService vacancyService;

    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @GetMapping
    public ResponseEntity<List<Vacancy>>  findAll() {
        List<Vacancy> vacancys = vacancyService.findAll();
        return ResponseEntity.ok().body(vacancys);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vacancy> findById(@PathVariable Long id) {
        Vacancy vacancy = vacancyService.findById(id);
        return ResponseEntity.ok().body(vacancy);
    }

    @GetMapping("/vacancy-by-enterprise/{enterpriseId}")
    public ResponseEntity<List<VacancyResponse>> findByEnterpiseid(@PathVariable Long enterpriseId) {
        List<VacancyResponse> vacancyResponse = vacancyService.findByEnterpriseId(enterpriseId);
        return ResponseEntity.ok().body(vacancyResponse);
    }

    @PostMapping("/{empresaId}/create-vacancy")
    public ResponseEntity<Vacancy> createVacancy(@RequestBody VacancyCreateDTO objDto, @PathVariable Long empresaId) {
        Vacancy vacancy = vacancyService.createVacany(empresaId, objDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(vacancy.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}/rejected")
    public ResponseEntity<Vacancy> rejectVacancy(@PathVariable Long id) {
        Vacancy vacancy = vacancyService.updateIfRejected(id);
        return ResponseEntity.ok().body(vacancy);
    }

    @PutMapping("/{id}/approved")
    public ResponseEntity<Vacancy> approveVacancy(@PathVariable Long id) {
        Vacancy vacancy = vacancyService.updateIfApproved(id);
        return ResponseEntity.ok().body(vacancy);
    }
}
