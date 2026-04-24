package com.example.demo.services;

import com.example.demo.entities.StudyArea;
import com.example.demo.repositories.StudyAreaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyAreaService {

    private final StudyAreaRepository studyAreaRepository;

    public StudyAreaService(StudyAreaRepository studyAreaRepository) {
        this.studyAreaRepository = studyAreaRepository;
    }

    public List<StudyArea> findAll() {
        return studyAreaRepository.findAll();
    }

}
