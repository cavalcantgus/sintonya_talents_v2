package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "education")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String educationalInstitution;
    private String diploma;
//    private StudyArea studyArea;
    private LocalDate startDate;
    private LocalDate endDate;
    private String note;
    private String description;

}
