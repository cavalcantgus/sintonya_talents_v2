package com.example.demo.controllers;

import com.example.demo.entities.Sector;
import com.example.demo.services.SectorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sectors")
public class SectorController {

    private final SectorService sectorService;

    public SectorController(SectorService sectorService) {
        this.sectorService = sectorService;
    }

    @GetMapping
    public ResponseEntity<List<Sector>> findAll() {
        List<Sector> sectors = sectorService.findAll();
        return ResponseEntity.ok().body(sectors);
    }
}
