package com.example.demo.entities;

import com.example.demo.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "job_application")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @OneToMany(
            mappedBy = "jobApplication",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ApplicationStageResult> applicationStageResults = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "vacancy_id", nullable = false)
    private Vacancy vacancy;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
}
