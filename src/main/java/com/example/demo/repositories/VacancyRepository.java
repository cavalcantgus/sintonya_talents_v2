package com.example.demo.repositories;

import com.example.demo.entities.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

    List<Vacancy> findByEnterpriseId(Long id);
    Vacancy findBySelectionStagesId(Long id);

    @Query("""
    SELECT v FROM Vacancy v
    WHERE v.vacancyStatus = 'OPEN'
    AND (v.expirationDate IS NULL or v.expirationDate >= CURRENT_DATE)
    ORDER BY v.publicationDate DESC
""")
    List<Vacancy> findAllActive();
}
