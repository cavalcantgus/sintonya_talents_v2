package com.example.demo.controllers;

import com.example.demo.dto.SelectionProcessCreateDTO;
import com.example.demo.dto.SelectionProcessResponse;
import com.example.demo.dto.VacancyCreateDTO;
import com.example.demo.entities.SelectionProcess;
import com.example.demo.entities.Vacancy;
import com.example.demo.services.SelectionProcessService;
import com.example.demo.services.VacancyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/selection-process")
public class SelectionProcessController {

    private final SelectionProcessService selectionProcessService;

    public SelectionProcessController(SelectionProcessService selectionProcessService) {
        this.selectionProcessService = selectionProcessService;
    }

    @GetMapping
    public ResponseEntity<List<SelectionProcess>> findAll() {
        List<SelectionProcess> selectionProcess = selectionProcessService.findAll();
        return ResponseEntity.ok().body(selectionProcess);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SelectionProcess> findById(@PathVariable Long id) {
        SelectionProcess selectionProcess = selectionProcessService.findById(id);
        return ResponseEntity.ok().body(selectionProcess);
    }

    @PostMapping("/{vacancyId}/create-selection-process")
    public ResponseEntity<SelectionProcessResponse> createVacancy(@RequestBody SelectionProcessCreateDTO objDto, @PathVariable Long vacancyId) {
        SelectionProcessResponse selectionProcess = selectionProcessService.insert(vacancyId, objDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(selectionProcess.id()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
