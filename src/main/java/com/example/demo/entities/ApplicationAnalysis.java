package com.example.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "application_analysis")
public class ApplicationAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "job_application_id")
    private JobApplication jobApplication;

    private Double finalScore;
    private String compatibilityLevel;

    @Column(columnDefinition = "TEXT")
    private String finalFeedback;
}
