package com.example.demo.controllers;

import com.example.demo.entities.Institution;
import com.example.demo.services.InstitutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/institutions")
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @GetMapping
    public ResponseEntity<List<Institution>> findAll() {
        List<Institution> institutions = institutionService.findAll();
        return ResponseEntity.ok().body(institutions);
    }
}
