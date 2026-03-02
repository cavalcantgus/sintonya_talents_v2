package com.example.demo.services;

import com.example.demo.entities.SkillBase;
import com.example.demo.repositories.SkillBaseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillBaseService {

    private final SkillBaseRepository skillBaseRepository;

    public SkillBaseService(SkillBaseRepository skillBaseRepository) {
        this.skillBaseRepository = skillBaseRepository;
    }

    public List<SkillBase> findAll() {
        return skillBaseRepository.findAll();
    }

    public SkillBase findById(Long id) {
        return skillBaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Habilidade não encontrada"));
    }

    public SkillBase insert(SkillBase obj) {
        return skillBaseRepository.save(obj);
    }
}
