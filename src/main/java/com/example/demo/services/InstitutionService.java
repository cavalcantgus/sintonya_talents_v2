package com.example.demo.services;

import com.example.demo.entities.Institution;
import com.example.demo.repositories.InstitutionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstitutionService {

    private final InstitutionRepository institutionRepository;

    public InstitutionService(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    public List<Institution> findAll() {
        return institutionRepository.findAll();
    }
}
