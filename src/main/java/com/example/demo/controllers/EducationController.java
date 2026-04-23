package com.example.demo.controllers;

import com.example.demo.dto.EducationCreateDTO;
import com.example.demo.dto.EducationResponse;
import com.example.demo.dto.EducationUpdateDTO;
import com.example.demo.entities.Education;
import com.example.demo.services.EducationService;
import org.apache.coyote.Response;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/educations")
public class EducationController {

    private final EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @GetMapping
    public ResponseEntity<List<EducationResponse>> findALl() {
        List<EducationResponse> educations = educationService.findAll();
        return ResponseEntity.ok().body(educations);
    }

    @GetMapping("/id")
    public ResponseEntity<EducationResponse> findById(@PathVariable Long id) {
        EducationResponse education = educationService.findById(id);
        return ResponseEntity.ok().body(education);
    }

    @PostMapping("/education/candidate/{candidateId}")
    public ResponseEntity<EducationResponse> insert(@RequestBody EducationCreateDTO objDto, @PathVariable Long candidateId) {
        EducationResponse education = educationService.insert(objDto, candidateId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(education.id()).toUri();
        return ResponseEntity.created(uri).body(education);
    }

    @PutMapping("/education/update/{id}")
    public ResponseEntity<EducationResponse> update(@PathVariable Long id, @RequestBody EducationUpdateDTO objDto) {
        EducationResponse education = educationService.update(objDto, id);
        return ResponseEntity.ok().body(education);
    }

    @DeleteMapping("/education/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        educationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

