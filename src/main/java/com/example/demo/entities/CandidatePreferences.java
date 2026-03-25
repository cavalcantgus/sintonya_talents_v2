package com.example.demo.entities;

import com.example.demo.enums.VacancyType;
import com.example.demo.enums.WorkModality;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.jdbc.Work;

@Entity
@Table(name = "candidate_preferences")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidatePreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @Enumerated(EnumType.STRING)
    private WorkModality modality;

    @Enumerated(EnumType.STRING)
    private VacancyType vacancyType;

    private Long totalExperience;
}
