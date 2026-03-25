package com.example.demo.repositories;

import com.example.demo.entities.Sector;
import com.example.demo.entities.Vacancy;
import com.example.demo.enums.WorkModality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query(
            """
    SELECT v FROM Vacancy v
    WHERE v.vacancyStatus = 'OPEN'
    AND (v.expirationDate IS NULL OR v.expirationDate >= CURRENT_DATE)
    AND v.closeAt IS NULL
    AND (v.sector = :sector
    OR v.modalityType = :modality
    OR v.locality = :locality)
    ORDER BY v.publicactionDate DESC
"""
    )
    List<Vacancy> findCandidatesForScoring(
            @Param("sector") Sector sector,
            @Param("modality")WorkModality modality,
            @Param("locality") String locality
    );
}
