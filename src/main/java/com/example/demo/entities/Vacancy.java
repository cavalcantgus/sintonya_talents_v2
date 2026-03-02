package com.example.demo.entities;

import com.example.demo.enums.VacancyStatus;
import com.example.demo.enums.VacancyType;
import com.example.demo.enums.WorkModality;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Enumerated(EnumType.STRING)
    private VacancyStatus vacancyStatus;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
