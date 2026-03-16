package com.example.demo.repositories;

import com.example.demo.entities.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

    List<Vacancy> findByEnterpriseId(Long id);
    Vacancy findBySelectionStagesId(Long id);

}
