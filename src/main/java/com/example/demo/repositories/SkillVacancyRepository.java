package com.example.demo.repositories;

import com.example.demo.entities.SkillVacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface SkillVacancyRepository extends JpaRepository<SkillVacancy, Long> {

    @Query("""
    SELECT sb.id
    FROM SkillVacancy s
    JOIN s.skillBase sb
    WHERE s.vacancy.id = :vacancyId
    AND s.required IS TRUE
""")
    Set<Long> findRequiredSkillIdsByVacancy(@Param("vacancyId") Long vacancyId);

    @Query("""
    SELECT sb.id
    FROM SkillVacancy s
    JOIN s.skillBase sb
    WHERE s.vacancy.id = :vacancyId
    AND s.required IS FALSE
""")
    Set<Long> findNonRequiredSkillIdsByVacancy(@Param("vacancyId") Long vacancyId);
}
