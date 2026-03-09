package com.example.demo.repositories;

import com.example.demo.entities.SelectionStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SelectionStageRepository extends JpaRepository<SelectionStage, Long> {
    List<SelectionStage> findByVacancyId(Long id);
}
