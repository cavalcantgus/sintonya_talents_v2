package com.example.demo.entities;

import com.example.demo.enums.VacancyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity()
@Table(name = "candidates")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private Profile profile;

    @Column(nullable = false)
    private String fullName;
    private String gender;
    private String raceEthnicity;
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String contact;

    @Enumerated(EnumType.STRING)
    private VacancyType availability;

    private String sector;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
