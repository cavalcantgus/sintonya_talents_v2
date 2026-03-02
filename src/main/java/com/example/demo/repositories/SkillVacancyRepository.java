package com.example.demo.repositories;

import com.example.demo.entities.SkillVacancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillVacancyRepository extends JpaRepository<SkillVacancy, Long> {
}
