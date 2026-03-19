package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "application_analysis")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "job_application_id")
    private JobApplication jobApplication;

    @OneToMany(
            mappedBy = "analysis",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ApplicationAnalysisDimension> applicationAnalysesDimensions = new HashSet<>();

    private Double finalScore;
    private String compatibilityLevel;

    @Column(columnDefinition = "TEXT")
    private String finalFeedback;
}
