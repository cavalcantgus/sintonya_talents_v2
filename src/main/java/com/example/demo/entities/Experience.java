package com.example.demo.entities;

import com.example.demo.enums.VacancyType;
import com.example.demo.enums.WorkModality;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "experiences")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    private String title;

    @Enumerated(EnumType.STRING)
    private VacancyType workType;

    private String organization;
    private LocalDate startDate;
    private LocalDate endDate;
    private String locality;
    private boolean isCurrent;

    @OneToMany(
            mappedBy = "experience",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<SkillCandidate> experienceSkills = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private WorkModality workModality;

    private String description;
}
