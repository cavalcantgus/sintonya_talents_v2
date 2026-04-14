package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidate_profile_score")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "candidate_id", nullable = false, unique = true)
    private Candidate candidate;

    @Column(nullable = false)
    private Double totalScore;

    private Double profileCompletenessScore;
    private Double skillsScore;
    private Double certificationScore;
    private Double experienceScore;
    private Double skillValidationScore;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Quando foi calculado de fato (diferente do updated_at de infra)
    @Column(name = "last_calculated_at")
    private LocalDateTime lastCalculatedAt;
}
