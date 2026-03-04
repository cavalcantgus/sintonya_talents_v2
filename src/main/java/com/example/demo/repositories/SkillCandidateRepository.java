package com.example.demo.repositories;

import com.example.demo.entities.SkillCandidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillCandidateRepository extends JpaRepository<SkillCandidate, Long> {

    boolean existsByCandidateIdAndSkillBaseId(Long candidateId, Long skillBaseId);
}
