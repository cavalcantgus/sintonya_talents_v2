package com.example.demo.entities;

import com.example.demo.enums.ExperienceRange;
import com.example.demo.enums.VacancyStatus;
import com.example.demo.enums.VacancyType;
import com.example.demo.enums.WorkModality;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vacancys")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise;

    @ManyToOne
    @JoinColumn(name = "sector_id")
    private Sector sector;

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false, unique = true)
    private Post post;

    @OneToMany(
            mappedBy = "vacancy",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<SelectionStage> selectionStages = new HashSet<>();

    private String title;
    private String description;
    private String position;

    @Enumerated(EnumType.STRING)
    private VacancyType vacancyType;

    private String locality;

    @Enumerated(EnumType.STRING)
    private WorkModality modalityType;

    private LocalDate publicationDate;
    private LocalDate expirationDate;
    private LocalDateTime closedAt;
    @Column(name = "has_a_technical_test")
    private boolean technicalTest;

    @Column(name = "has_a_behavioral_test")
    private boolean behavioralTest;

    @Column(name = "required_certificate")
    private boolean certificateRequired;

    @Enumerated(EnumType.STRING)
    private ExperienceRange experienceRange;

    private Long minExperienceRange;
    private Long maxExperienceRange;

    @Enumerated(EnumType.STRING)
    private VacancyStatus vacancyStatus;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
