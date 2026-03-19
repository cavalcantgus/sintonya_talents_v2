package com.example.demo.repositories;

import com.example.demo.entities.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    @Query(value = """
    SELECT e.*
    FROM experiences e
    WHERE (
        EXTRACT(YEAR FROM AGE(COALESCE(e.end_date, CURRENT_DATE), e.start_date)) * 12 +
        EXTRACT(MONTH FROM AGE(COALESCE(e.end_date, CURRENT_DATE), e.start_date))
    ) BETWEEN :min AND :max
""", nativeQuery = true)
    List<Experience> findMatchingExperiences(
            @Param("min") int min,
            @Param("max") int max
    );
}
