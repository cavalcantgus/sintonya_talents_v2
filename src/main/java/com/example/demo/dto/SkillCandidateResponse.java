package com.example.demo.dto;

import com.example.demo.entities.SkillCandidate;

public record SkillCandidateResponse(
        Long id,
        Long skillId,
        String skillTitle,
        String skillType,
        String skillLevel
) {
    public static SkillCandidateResponse fromEntity(SkillCandidate entity) {
        return new SkillCandidateResponse(
                entity.getId(),
                entity.getSkillBase().getId(),
                entity.getSkillBase().getTitle(),
                entity.getSkillBase().getSkillType().name(),
                entity.getSkillLevel() != null ? entity.getSkillLevel().name() : null
        );
    }
}