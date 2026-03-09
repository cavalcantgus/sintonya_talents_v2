package com.example.demo.controllers;

import com.example.demo.dto.SelectionProcessResponse;
import com.example.demo.dto.SelectionStageCreateDTO;
import com.example.demo.services.SelectionStageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/selection-process")
public class SelectionStageController {

    private final SelectionStageService selectionStageService;

    public SelectionStageController(SelectionStageService selectionStageService) {
        this.selectionStageService = selectionStageService;
    }
//
//    @GetMapping
//    public ResponseEntity<List<SelectionProcess>> findAll() {
//        List<SelectionProcess> selectionProcess = selectionProcessService.findAll();
//        return ResponseEntity.ok().body(selectionProcess);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<SelectionProcess> findById(@PathVariable Long id) {
//        SelectionProcess selectionProcess = selectionProcessService.findById(id);
//        return ResponseEntity.ok().body(selectionProcess);
//    }

    @PutMapping("/{vacancyId}/create-selection-process")
    public ResponseEntity<?> addSelectionStage(@PathVariable Long vacancyId, @RequestBody List<SelectionStageCreateDTO> objDto) {
        selectionStageService.insert(vacancyId, objDto);
        return ResponseEntity.ok().build();
    }
}
