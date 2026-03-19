package com.example.demo.services;

import com.example.demo.entities.Sector;
import com.example.demo.repositories.SectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorService {

    private final SectorRepository sectorRepository;

    public SectorService(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    public List<Sector> findAll() {
        return sectorRepository.findAll();
    }
}
