package com.example.demo.controllers;

import com.example.demo.entities.Institution;
import com.example.demo.entities.StudyArea;
import com.example.demo.services.InstitutionService;
import com.example.demo.services.StudyAreaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/study-areas")
public class StudyAreaController {

    private final StudyAreaService studyAreaService;

    public StudyAreaController(StudyAreaService studyAreaService) {
        this.studyAreaService = studyAreaService;
    }

    @GetMapping
    public ResponseEntity<List<StudyArea>> findAll() {
        List<StudyArea> studyAreas = studyAreaService.findAll();
        return ResponseEntity.ok().body(studyAreas);
    }
}
