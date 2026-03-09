package com.example.demo.repositories;

import com.example.demo.entities.SkillCandidate;
import com.example.demo.enums.SkillSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface SkillCandidateRepository extends JpaRepository<SkillCandidate, Long> {

    @Query("""
        SELECT sc.skillBase.id
        FROM SkillCandidate sc
        WHERE sc.candidate.id = :candidateId
        AND sc.source = :source
        """)
    Set<Long> findSkillIdsByCandidateIdAndSource(Long candidateId, SkillSource source);
}
